package fi.vm.sade.viestintapalvelu.letter;

import static java.util.concurrent.TimeUnit.SECONDS;
import com.google.common.collect.Lists;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.download.cache.AWSS3ClientFactory;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.types.ContentTypes;
import fi.vm.sade.viestintapalvelu.util.S3Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface LetterPublisher {
    int publishLetterBatch(long letterBatchId) throws Exception;
}

@Service
@Profile("!aws")
class LocalLetterPublisher implements LetterPublisher {

    @Value("${viestintapalvelu.letter.publish.dir}")
    private File letterPublishDir;

    private final LetterBatchDAO letterBatchDAO;
    private final LetterReceiverLetterDAO letterReceiverLetterDAO;

    public static final Logger logger = LoggerFactory.getLogger(LocalLetterPublisher.class);

    @Autowired
    public LocalLetterPublisher(LetterBatchDAO letterBatchDAO,
                                LetterReceiverLetterDAO letterReceiverLetterDAO) {
        this.letterBatchDAO = letterBatchDAO;
        this.letterReceiverLetterDAO = letterReceiverLetterDAO;
    }

    @Override
    public int publishLetterBatch(long letterBatchId) {
        List<Long> letters = letterBatchDAO.getUnpublishedLetterIds(letterBatchId);
        if(letters.size() > 0) {
            new Thread(new LetterPublishTask(letterBatchId, letters)).start();
            return letters.size();
        } else {
            return 0;
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void publishLetter(long letterId, File letterBatchPublishDir) {
        File tempPdfFile = null;
        File finalPdfFile = null;
        try {
            LetterReceiverLetter letter = letterReceiverLetterDAO.read(letterId);
            tempPdfFile = new File(letterBatchPublishDir, letter.getLetterReceivers().getOidApplication() + "_partial_" + System.currentTimeMillis() + ".pdf");
            finalPdfFile = new File(letterBatchPublishDir, letter.getLetterReceivers().getOidApplication() + ".pdf");
            FileOutputStream out = new FileOutputStream(tempPdfFile);
            out.write(letter.getLetter());
            out.close();
            letterReceiverLetterDAO.markAsPublished(letter.getId());
            tempPdfFile.renameTo(finalPdfFile);
        } catch (Exception e) {
            if(tempPdfFile != null) {
                tempPdfFile.delete();
            }
            throw new RuntimeException("Error publishing letter id " + letterId + (finalPdfFile == null ? "" : " to " + finalPdfFile), e);
        }
    }

    private class LetterPublishTask implements Runnable {

        private final long letterBatchId;
        private final List<Long> letterIds;

        protected LetterPublishTask(long letterBatchId, List<Long> letterIds) {
            this.letterBatchId = letterBatchId;
            this.letterIds = letterIds;
        }

        @Override
        public void run() {
            LetterBatch letterBatch = letterBatchDAO.read(letterBatchId);
            String subFolderName = StringUtils.isEmpty(letterBatch.getApplicationPeriod()) ? String.valueOf(letterBatchId) : letterBatch.getApplicationPeriod().trim();
            File letterBatchPublishDir = new File(letterPublishDir, subFolderName);
            logger.info("Publishing {} letters from letter batch {} to dir {}", letterIds.size(), letterBatchId, letterBatchPublishDir);
            letterBatchPublishDir.mkdirs();
            int count = 0;
            try {
                for (Long letterId : letterIds) {
                    publishLetter(letterId, letterBatchPublishDir);
                    count++;
                    if (count % 100 == 0) {
                        logger.info("Published {}/{} letters from letter batch {} to dir {}", count, letterIds.size(), letterBatchId, letterBatchPublishDir);
                    }
                }
            }
            catch (Exception e) {
                logger.error("Letter batch " + letterBatchId + " publish failed. Published " + count + "/" + letterIds.size(), e);
                throw e;
            }
            logger.info("Published successfully {} letters from letter batch {} to dir {}", count, letterBatchId, letterBatchPublishDir);

        }
    }
}

@Service
@Profile("aws")
class S3LetterPublisher implements LetterPublisher {
    private static final int MAX_RETRIES = 5;
    private static final int LETTER_PARTITION_SIZE = 20;
    private static final int PUBLISH_LOGGING_FREQUENCY = 1000;

    private final LetterBatchDAO letterBatchDAO;
    private final LetterReceiverLetterDAO letterReceiverLetterDAO;
    private static final Logger logger = LoggerFactory.getLogger(S3LetterPublisher.class);

    private final String bucket;
    private final String region;

    private final AWSS3ClientFactory clientFactory;

    @Autowired
    S3LetterPublisher(LetterBatchDAO letterBatchDAO,
                      LetterReceiverLetterDAO letterReceiverLetterDAO,
                      @Value("${viestintapalvelu.downloadfiles.s3.bucket}") String bucket,
                      @Value("${viestintapalvelu.downloadfiles.s3.region}") String region,
                      AWSS3ClientFactory clientFactory) {
        this.letterBatchDAO = letterBatchDAO;
        this.letterReceiverLetterDAO = letterReceiverLetterDAO;
        this.bucket = bucket;
        this.region = region;
        this.clientFactory = clientFactory;
    }

    @PostConstruct
    public void init() {
        logger.info("Region {}", region);
        logger.info("Bucket {}", bucket);
        boolean success = S3Utils.canConnectToBucket(getClient(), bucket);
        if(!success) {
            logger.error("LetterPublisher S3 client could not connect to S3 bucket {} in region {}", bucket, region);
        }
    }

    @Override
    @Transactional
    public int publishLetterBatch(long letterBatchId) throws Exception {
        List<Long> unpublishedLetterIds = letterBatchDAO.getUnpublishedLetterIds(letterBatchId);
        List<List<Long>> letterPartitions = Lists.partition(unpublishedLetterIds, LETTER_PARTITION_SIZE);
        logger.info(String.format("Publishing letter batch %d of size %d in %d partitions of up to %d each...",
            letterBatchId, unpublishedLetterIds.size(), letterPartitions.size(), LETTER_PARTITION_SIZE));
        int counter = 0;
        for(List<Long> letters : letterPartitions) {
            List<CompletableFuture<PutObjectResponse>> completableFutures = publishLettersS3(letterBatchId, letters);
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()])).get();
            counter += letters.size();
            if ((counter % PUBLISH_LOGGING_FREQUENCY) == 0) {
                logger.info(String.format("...published %d/%d letters of batch %d so far...", counter, unpublishedLetterIds.size(), letterBatchId));
            }
        }
        logger.info(String.format("...published %d/%d letters of batch %d .", counter, unpublishedLetterIds.size(), letterBatchId));
        return counter;
    }

    private List<CompletableFuture<PutObjectResponse>> publishLettersS3(long letterBatchId, List<Long> letterIds) {
        final LetterBatch letterBatch = letterBatchDAO.read(letterBatchId);

        String subFolderName = StringUtils.isEmpty(letterBatch.getApplicationPeriod()) ? String.valueOf(letterBatchId) : letterBatch.getApplicationPeriod().trim();

        return letterReceiverLetterDAO
                .findByIds(letterIds)
                .stream()
                .peek(S3LetterPublisher::validateFileSuffix)
                .map(letterReceiverLetter -> {
                    try {
                        return addFileObject(letterReceiverLetter, subFolderName);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    } catch (Exception e) {
                        logger.error("Ei voitu lähettää S3:een tiedostoa vastaanottajakirjeestä {}", letterReceiverLetter.getId(), e);
                        throw e;
                    }
                })
                .collect(Collectors.toList());
    }

    private static void validateFileSuffix(final LetterReceiverLetter letterReceiverLetter) {
        final Optional<String> fileSuffix = ContentTypes.getFileSuffix(letterReceiverLetter.getContentType());
        if (!fileSuffix.isPresent()) {
            throw new NullPointerException("Vastaanottajakirjeellä " + letterReceiverLetter.getId() + " oli tuntematon sisältötyyppi: " + letterReceiverLetter.getContentType());
        }
    }

    private CompletableFuture<PutObjectResponse> addFileObject(final LetterReceiverLetter letter, String folderName) throws IOException {
        Supplier<CompletableFuture<PutObjectResponse>> addFileObjectF = () -> {
            try {
                return addFileObjectF(
                    letter,
                    folderName,
                    letter.getLetterReceivers().getOidApplication(),
                    ContentTypes.getFileSuffix(letter.getContentType()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        return executeWithRetry(addFileObjectF, String.format("Hakijan %s kirjeen julkaisu", letter.getLetterReceivers().getOidPerson()));
    }

    private CompletableFuture<PutObjectResponse> addFileObjectF(LetterReceiverLetter letter, String folderName, String oidApplication, Optional<String> fileSuffix) throws IOException {
        try {
            return addFileObject(letter, folderName, oidApplication, fileSuffix);
        } catch (Exception e) {
            return failedFuture(e);
        }
    }

    private CompletableFuture<PutObjectResponse> addFileObject(LetterReceiverLetter letter, String folderName, String oidApplication, Optional<String> fileSuffix) throws IOException {
        final Path tempFile = Files.createTempFile(oidApplication, fileSuffix.get());
        Files.write(tempFile, letter.getLetter(), StandardOpenOption.WRITE);

        Long length = (long) letter.getLetter().length;
        Map<String, String> metadata = new HashMap<>();
        String id = folderName + File.separator + oidApplication + fileSuffix.get();
        final PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .contentType(letter.getContentType())
                .contentEncoding("UTF-8")
                .contentLength(length)
                .metadata(metadata)
                .key(id)
                .build();

        AsyncRequestBody asyncRequestProvider = AsyncRequestBody.fromFile(tempFile);

        S3AsyncClient client = getClient();
        return client.putObject(request, asyncRequestProvider).whenComplete((putObjectResponse, throwable) -> {
            if(putObjectResponse == null) {
                logger.error("Error saving LetterReceiverLetter {} to S3", letter.getId(), throwable);
            }
            letterReceiverLetterDAO.markAsPublished(letter.getId());
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                logger.error("Error deleting temp file {}", tempFile.toAbsolutePath().toString(), e);
            }
            try {
                client.close();
            } catch (Exception e) {
                logger.error("Error closing S3 client", e);
            }
        });
    }

    private S3AsyncClient getClient() {
        return clientFactory.getClient(Region.of(region));
    }

    private static <R> CompletableFuture<R> executeWithRetry(Supplier<CompletableFuture<R>> action, String tunniste) {
        CompletableFuture<R> actionFuture;
        try {
            actionFuture = action.get();
        } catch (Exception e) {
            logger.warn(
                String.format(
                    "%s : Operaatiossa tapahtui virhe '%s', yritetään uudelleen korkeintaan %s kertaa pitenevin välein",
                    tunniste,
                    e.getMessage(),
                    MAX_RETRIES),
                e);
            return retry(action, e, 0, tunniste);
        }
        return actionFuture
            .handleAsync((r, t) -> {
                if (t != null) {
                    return retry(action, t, 0, tunniste);
                } else {
                    return CompletableFuture.completedFuture(r);
                }
            }).thenCompose(java.util.function.Function.identity())
            .whenComplete((r, t) -> {
                if (t != null) {
                    logger.info(String.format("%s : Kaikki uudelleenyritykset (%s kpl) on käytetty, ei yritetä enää. Virhe: %s", tunniste, MAX_RETRIES, t.getMessage()));
                }
            });
    }

    private static <R> CompletableFuture<R> retry(Supplier<CompletableFuture<R>> action, Throwable throwable, int retry, String tunniste) {
        int secondsToWaitMultiplier = 10;
        final int secondsToWait = retry * secondsToWaitMultiplier;
        logger.warn(
            String.format(
                "%s : Operaatiossa tapahtui virhe '%s', uudelleenyritys # %s/%s , %s sekunnin päästä",
                tunniste,
                throwable.getMessage(),
                retry + 1,
                MAX_RETRIES,
                secondsToWait),
            throwable);
        if (retry >= MAX_RETRIES) return failedFuture(throwable);
        CompletableFuture<R> actionFuture;
        try {
            actionFuture = action.get();
        } catch (Exception e) {
            return createRetryWithDelay(action, throwable, retry, tunniste, secondsToWait);
        }

        return actionFuture
            .handleAsync((r, t) -> {
                if (t != null) {
                    throwable.addSuppressed(t);
                    return createRetryWithDelay(action, throwable, retry, tunniste, secondsToWait);
                } else {
                    logger.info(
                        String.format(
                            "%s : onnistuttiin uudelleenyrityksellä %d/%d",
                            tunniste,
                            retry + 1,
                            MAX_RETRIES));
                }
                return CompletableFuture.completedFuture(r);
            }).thenCompose(java.util.function.Function.identity());
    }

    private static <R> CompletableFuture<R> createRetryWithDelay(Supplier<CompletableFuture<R>> action, Throwable throwable, int retry, String tunniste, int secondsToWait) {
        Executor delayedExecutor = new DelayedExecutor(secondsToWait, SECONDS, ASYNC_POOL);
        return CompletableFuture.supplyAsync(() -> "OK", delayedExecutor)
            .thenComposeAsync(x -> retry(action, throwable, retry + 1, tunniste));
    }

    /**
     * TODO : Poista, kun saadaan käyttöön Java 9 tai uudempi
     */
    @Deprecated
    private static <T> CompletableFuture<T> failedFuture(Throwable t) {
        final CompletableFuture<T> cf = new CompletableFuture<>();
        cf.completeExceptionally(t);
        return cf;
    }

    /**
     * TODO : Poista, kun saadaan käyttöön Java 9 tai uudempi
     */
    @Deprecated
    private static final class DelayedExecutor implements Executor {
            final long delay;
            final TimeUnit unit;
            final Executor executor;
            DelayedExecutor(long delay, TimeUnit unit, Executor executor) {
                this.delay = delay; this.unit = unit; this.executor = executor;
            }
            public void execute(Runnable r) {
                Delayer.delay(new TaskSubmitter(executor, r), delay, unit);
            }
        }

    /**
     * TODO : Poista, kun saadaan käyttöön Java 9 tai uudempi
     */
    @Deprecated
    private static final class Delayer {
        static ScheduledFuture<?> delay(Runnable command, long delay,
                                        TimeUnit unit) {
            return delayer.schedule(command, delay, unit);
        }

        static final class DaemonThreadFactory implements ThreadFactory {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("CompletableFutureDelayScheduler");
                return t;
            }
        }

        static final ScheduledThreadPoolExecutor delayer;
        static {
            (delayer = new ScheduledThreadPoolExecutor(
                1, new DaemonThreadFactory())).
                setRemoveOnCancelPolicy(true);
        }
    }

    /**
     * TODO : Poista, kun saadaan käyttöön Java 9 tai uudempi
     */
    @Deprecated
    private static final class TaskSubmitter implements Runnable {
        final Executor executor;
        final Runnable action;
        TaskSubmitter(Executor executor, Runnable action) {
            this.executor = executor;
            this.action = action;
        }
        public void run() { executor.execute(action); }
    }

    private static final boolean USE_COMMON_POOL =
        (ForkJoinPool.getCommonPoolParallelism() > 1);

    /**
     * Default executor -- ForkJoinPool.commonPool() unless it cannot
     * support parallelism.
     */
    private static final Executor ASYNC_POOL = USE_COMMON_POOL ?
        ForkJoinPool.commonPool() : new ThreadPerTaskExecutor();

    /**
     * TODO : Poista, kun saadaan käyttöön Java 9 tai uudempi
     */
    @Deprecated
    static final class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) { new Thread(r).start(); }
    }
}
