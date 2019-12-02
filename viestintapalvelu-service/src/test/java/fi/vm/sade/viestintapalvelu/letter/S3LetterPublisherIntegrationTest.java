package fi.vm.sade.viestintapalvelu.letter;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.download.cache.AWSS3ClientFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * To use this test, first start localstack:
 *
 *   docker run --name localstack -d -p 5000:5000 -e SERVICES=s3:5000 localstack/localstack
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = S3LetterPublisherTest.LetterPublishTestConfig.class)
@TestPropertySource("classpath:test.properties")
@Ignore // TODO: running localstack should be included in automatic test runs
public class S3LetterPublisherIntegrationTest {

    private static final String BUCKET_NAME = "mock-bucket";
    private static final String REGION_NAME = "eu-west-1";
    private S3LetterPublisher s3LetterPublisher;

    private static PublisherIntegrationTestClientFactory clientFactory = new PublisherIntegrationTestClientFactory();

    @Autowired
    private S3LetterPublisherTest.LetterPublishTestConfig letterPublishTestConfig;
    private LetterBatchDAO letterBatchDAO;
    private LetterReceiverLetterDAO letterReceiverLetterDAO;

    @Before
    public void initialiseTestBucketEtc() throws Exception {
       S3AsyncClient s3client = clientFactory.getClient(Region.of(REGION_NAME));
        ensureBucketExists(s3client);

        s3LetterPublisher = luoS3LetterPublisher(clientFactory);
    }

    /**
     * TODO : This could probably be done in a cleaner way :)
     */
    private void ensureBucketExists(S3AsyncClient s3client) throws Exception {
        try {
            s3client.listObjects(ListObjectsRequest.builder().bucket(BUCKET_NAME).build()).get(10, SECONDS);
        } catch (Exception e) {
            s3client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build()).get(10, SECONDS);
        }
    }

    @Test
    public void testInit() {
        s3LetterPublisher.init();
    }

    @Test
    public void publishLetterBatch() throws Exception {
        s3LetterPublisher.publishLetterBatch(1l);
    }

    @Test
    public void s3ClientinHeittamistaPoikkeuksistaToivutaan() throws Exception {
        final S3AsyncClient poikkeuksenHeittavaClient = mock(S3AsyncClient.class);
        when(poikkeuksenHeittavaClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class)))
            .thenThrow(new RuntimeException("Testiräjähdys!"));

        final AtomicInteger virheitaViela = new AtomicInteger(2);
        s3LetterPublisher = luoS3LetterPublisher(new PublisherIntegrationTestClientFactory() {
            @Override
            public S3AsyncClient getClient(Region awsRegion) {
                if (virheitaViela.decrementAndGet() > 0) {
                    return poikkeuksenHeittavaClient;
                }
                return super.getClient(awsRegion);
            }
        });

        s3LetterPublisher.publishLetterBatch(1L);

        verify(poikkeuksenHeittavaClient).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
        verifyNoMoreInteractions(poikkeuksenHeittavaClient);

        int numberOfBatches = (125 / 20) + 1;
        int numberOfLetters = numberOfBatches; // in test data, there is only one letter per batch
        verify(letterReceiverLetterDAO, times(numberOfBatches)).findByIds(any());
        verify(letterReceiverLetterDAO, times(numberOfLetters)).markAsPublished(null);
        verifyNoMoreInteractions(letterReceiverLetterDAO);
    }

    @Test
    public void s3ClientinPalauttamistaVirheistaToivutaan() throws Exception {
        final S3AsyncClient virheFuturenPalauttavaClient = mock(S3AsyncClient.class);
        when(virheFuturenPalauttavaClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class)))
            .thenReturn(failedFuture(new RuntimeException("Testiräjähdys!")));

        final AtomicInteger virheitaViela = new AtomicInteger(3);
        s3LetterPublisher = luoS3LetterPublisher(new PublisherIntegrationTestClientFactory() {
            @Override
            public S3AsyncClient getClient(Region awsRegion) {
                if (virheitaViela.decrementAndGet() > 0) {
                    return virheFuturenPalauttavaClient;
                }
                return super.getClient(awsRegion);
            }
        });

        s3LetterPublisher.publishLetterBatch(1L);

        verify(virheFuturenPalauttavaClient, times(2)).putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class));
        verify(virheFuturenPalauttavaClient, times(2)).close();
        verifyNoMoreInteractions(virheFuturenPalauttavaClient);

        int numberOfBatches = (125 / 20) + 1;
        int numberOfLetters = numberOfBatches; // in test data, there is only one letter per batch
        verify(letterReceiverLetterDAO, times(numberOfBatches)).findByIds(any());
        verify(letterReceiverLetterDAO, times(numberOfLetters)).markAsPublished(null);
        verifyNoMoreInteractions(letterReceiverLetterDAO);
    }

    private S3LetterPublisher luoS3LetterPublisher(PublisherIntegrationTestClientFactory clientFactory) {
        letterBatchDAO = letterPublishTestConfig.getletterBatchDAO();
        letterReceiverLetterDAO = letterPublishTestConfig.getLetterReceiverLetterDAO();
        return new S3LetterPublisher(
            letterBatchDAO,
            letterReceiverLetterDAO,
            BUCKET_NAME,
            REGION_NAME,
            clientFactory);
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
}

class PublisherIntegrationTestClientFactory implements AWSS3ClientFactory {
    @Override
    public S3AsyncClient getClient(Region awsRegion) {
        return S3AsyncClient.builder()
            .region(awsRegion)
            .credentialsProvider(() -> AwsBasicCredentials.create("lol", "bal"))
            .endpointOverride(URI.create("http://localhost:5000"))
            .build();

    }
}
