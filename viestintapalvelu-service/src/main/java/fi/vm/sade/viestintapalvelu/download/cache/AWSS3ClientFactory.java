package fi.vm.sade.viestintapalvelu.download.cache;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.auth.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;


/**
 * Necessary for providing mocked clients in tests for the OphS3Client. Test sources contain factory implementation for
 * that context
 */
public interface AWSS3ClientFactory {
    S3AsyncClient getClient(Region awsRegion);
}


@Component
@Primary
class AWSClientFactoryImpl implements AWSS3ClientFactory {

    private static final AwsCredentialsProvider AWS_CREDENTIALS_PROVIDER = new DefaultCredentialsProvider();

    @Override
    public S3AsyncClient getClient(Region awsRegion) {
        return S3AsyncClient.builder()
                .region(awsRegion)
                .credentialsProvider(AWS_CREDENTIALS_PROVIDER)
                .build();
    }
}