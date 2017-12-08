package fi.vm.sade.viestintapalvelu.download.cache;

import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import software.amazon.awssdk.async.AsyncRequestProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:test.properties")
public class OphS3ClientTest {

    @Autowired
    Environment environment;

    @Autowired
    OphS3Client client;

    @Test
    public void init() {
        client.init();
    }

    @Test
    public void addFileObject() {
        Download download = new Download("application/pdf", "testfilename.pdf", "binary".getBytes());
        OphS3Client.AddObjectResponse<PutObjectResponse> res = client.addFileObject(download);
    }

    @Test
    public void addFileObjectWithDocumentId() {
        Download download = new Download("application/pdf", "testfilename.pdf", "binary".getBytes());
        UUID uuid = UUID.randomUUID();
        DocumentId id = new DocumentId(uuid.toString());
        OphS3Client.AddObjectResponse<PutObjectResponse> res = client.addFileObject(download, id);
    }

    @Test(expected = NoSuchKeyException.class)
    public void getObjectThatDoesNotExist() {
        DocumentId documentId = new DocumentId("mock_id");
        Download object = client.getObject(documentId, false);
    }

    @Test()
    public void getObject() {
        Download download = new Download("application/pdf", "testfilename.pdf", "binary".getBytes());
        String idStr = UUID.randomUUID().toString();
        DocumentId id = new DocumentId(idStr);
        client.addFileObject(download, id);
        Download object = client.getObject(id, false);
    }

    @Test
    public void listObjects() {
        Download download = new Download("application/pdf", "testfilename.pdf", "binary".getBytes());
        client.addFileObject(download);
        Download download2 = new Download("application/pdf", "testfilename2.pdf", "binary".getBytes());
        client.addFileObject(download2);

        List<Header> headers = client.listObjects();
        assertEquals(2,headers.size());
    }


}

class TestClientFactory implements AWSS3ClientFactory {

    private static Map<String, Object> mockS3Store = new HashMap<>();

    @Override
    public S3AsyncClient getClient(Region awsRegion) {
        S3AsyncClient mock = mock(S3AsyncClient.class);

        //head mocking
        when(mock.headBucket(any())).thenReturn(CompletableFuture.completedFuture(mock(HeadBucketResponse.class)));

        //add object mocking
        when(mock.putObject(any(), any(AsyncRequestProvider.class))).thenAnswer((call) -> {
            PutObjectRequest putrq = call.getArgumentAt(0, PutObjectRequest.class);
            mockS3Store.put(putrq.key(), putrq);
            return CompletableFuture.completedFuture(mock(PutObjectResponse.class));
        });

        //head object mocking
        when(mock.headObject(any(HeadObjectRequest.class))).thenAnswer((call) -> {
            HeadObjectRequest headrq = call.getArgumentAt(0, HeadObjectRequest.class);
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
        when(mock.listObjectsV2(any())).thenAnswer((call) -> {
           call.getArgumentAt(0, ListObjectsV2Request.class);
            List<S3Object> collect = mockS3Store.entrySet().stream().map(entry ->
                    S3Object.builder()
                    .key(entry.getKey())
                    .build()).collect(Collectors.toList());
            return CompletableFuture.completedFuture(ListObjectsV2Response.builder().contents(collect).build());
        });
        return mock;
    }
}

@Configuration
class TestConfig {

    @Autowired
    Environment environment;

    @Bean
    OphS3Client getOphS3Client() {
        return new OphS3Client(environment, new TestClientFactory());
    }


}