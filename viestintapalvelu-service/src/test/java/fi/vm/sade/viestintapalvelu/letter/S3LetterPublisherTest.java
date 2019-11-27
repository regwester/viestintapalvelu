package fi.vm.sade.viestintapalvelu.letter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.download.cache.AWSS3ClientFactory;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.types.ContentTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = S3LetterPublisherTest.LetterPublishTestConfig.class)
@TestPropertySource("classpath:test.properties")
public class S3LetterPublisherTest {

    @Autowired
    private S3LetterPublisher s3LetterPublisher;

    @Test
    public void testInit() {
        s3LetterPublisher.init();
    }

    @Test
    public void publishLetterBatch() throws Exception {
        s3LetterPublisher.publishLetterBatch(1l);
    }

    @Configuration
    static class LetterPublishTestConfig {

        @Autowired
        Environment environment;

        //@Bean
        LetterReceiverLetterDAO getLetterReceiverLetterDAO() {
            LetterReceiverLetterDAO letterReceiverLetterDAO = mock(LetterReceiverLetterDAO.class);


            List<LetterReceiverLetter> letterReceiverLetters = new ArrayList<>();
            LetterReceiverLetter letterReceiverLetter = new LetterReceiverLetter();
            LetterReceivers letterReceivers = new LetterReceivers();
            letterReceivers.setOidApplication("1.2.3.4");
            letterReceiverLetter.setLetterReceivers(letterReceivers);
            letterReceiverLetter.setLetter("letter".getBytes());
            letterReceiverLetter.setContentType(ContentTypes.CONTENT_TYPE_PDF);
            letterReceiverLetters.add(letterReceiverLetter);

            when(letterReceiverLetterDAO.findByIds(any())).thenReturn(letterReceiverLetters);


            return letterReceiverLetterDAO;
        }


        //@Bean
        LetterBatchDAO getletterBatchDAO() {
            LetterBatchDAO letterBatchDAO = mock(LetterBatchDAO.class);
            List<Long> ids = LongStream.range(0, 125).boxed().collect(Collectors.toList());
            when(letterBatchDAO.getUnpublishedLetterIds(anyLong())).thenReturn(ids);


            final fi.vm.sade.viestintapalvelu.model.LetterBatch batch1 = new fi.vm.sade.viestintapalvelu.model.LetterBatch();
            batch1.setId(0l);

            final fi.vm.sade.viestintapalvelu.model.LetterBatch batch2 = new fi.vm.sade.viestintapalvelu.model.LetterBatch();
            batch2.setId(1l);

            final fi.vm.sade.viestintapalvelu.model.LetterBatch batch3 = new fi.vm.sade.viestintapalvelu.model.LetterBatch();
            batch3.setId(2l);

            final Map<Long, fi.vm.sade.viestintapalvelu.model.LetterBatch> batches = new HashMap<>();
            batches.put(0l, batch1);
            batches.put(1l, batch1);
            batches.put(2l, batch3);

            when(letterBatchDAO.read(anyLong())).then(answer -> {
                long id = answer.getArgument(0, Long.class);

                return batches.get(id);
            });

            return letterBatchDAO;
        }

        @Bean
        S3LetterPublisher getS3LetterPublisher() {
            return new S3LetterPublisher(getletterBatchDAO(), getLetterReceiverLetterDAO(), "mock-bucket", "mock-region", new PublisherTestClientFactory());
        }


    }
}



class PublisherTestClientFactory implements AWSS3ClientFactory {

    private static Map<String, Object> mockS3Store = new HashMap<>();

    @Override
    public S3AsyncClient getClient(Region awsRegion) {
        S3AsyncClient mock = mock(S3AsyncClient.class);

        //head mocking
        when(mock.headBucket(any(HeadBucketRequest.class))).thenReturn(CompletableFuture.completedFuture(mock(HeadBucketResponse.class)));

        //add object mocking
        when(mock.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class))).thenAnswer((call) -> {
            PutObjectRequest putrq = call.getArgument(0, PutObjectRequest.class);
            mockS3Store.put(putrq.key(), putrq);
            return CompletableFuture.completedFuture(mock(PutObjectResponse.class));
        });

        //head object mocking
        when(mock.headObject(any(HeadObjectRequest.class))).thenAnswer((call) -> {
            HeadObjectRequest headrq = call.getArgument(0, HeadObjectRequest.class);
            String key = headrq.key();
            if(!mockS3Store.containsKey(key)) {
                throw NoSuchKeyException.builder().message("NoSuchKeyException in mock store").build();
            }

            PutObjectRequest o =(PutObjectRequest) mockS3Store.get(key);

            HeadObjectResponse res = HeadObjectResponse.builder()
                    .metadata(o.metadata())
                    .contentLength(o.contentLength())
                    .contentType(o.contentType())
                    .build();
            return CompletableFuture.completedFuture(res);
        });


        //list objects mock
        when(mock.listObjectsV2(any(ListObjectsV2Request.class))).thenAnswer((call) -> {
            call.getArgument(0, ListObjectsV2Request.class);
            List<S3Object> collect = mockS3Store.entrySet().stream().map(entry ->
                    S3Object.builder()
                            .key(entry.getKey())
                            .build()).collect(Collectors.toList());
            return CompletableFuture.completedFuture(ListObjectsV2Response.builder().contents(collect).build());
        });
        return mock;
    }
}
