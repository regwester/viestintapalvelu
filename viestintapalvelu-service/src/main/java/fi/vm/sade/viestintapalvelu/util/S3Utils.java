package fi.vm.sade.viestintapalvelu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;

import java.util.concurrent.CompletableFuture;

public class S3Utils {

    private S3Utils() {}

    private static final Logger log = LoggerFactory.getLogger(S3Utils.class);

    public static boolean canConnectToBucket(S3AsyncClient client, String bucket) {
        try {
            if (client == null) {
                log.error("Can not test connectivity with null client");
                return false;
            }
            CompletableFuture<HeadBucketResponse> resFut = client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            resFut.get(); // test if it completes exceptionally or not
        } catch (Exception e) {
            log.error("Error occurred while testing connectivity to S3", e);
            return false;
        }

        return true;
    }
}
