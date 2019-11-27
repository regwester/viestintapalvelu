package fi.vm.sade.viestintapalvelu.letter;

import static java.util.concurrent.TimeUnit.SECONDS;

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
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;

import java.net.URI;

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

    @Before
    public void initialiseTestBucketEtc() throws Exception {
       S3AsyncClient s3client = clientFactory.getClient(Region.of(REGION_NAME));
        ensureBucketExists(s3client);

        s3LetterPublisher = new S3LetterPublisher(
            letterPublishTestConfig.getletterBatchDAO(),
            letterPublishTestConfig.getLetterReceiverLetterDAO(),
            BUCKET_NAME,
            REGION_NAME,
            clientFactory);
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
