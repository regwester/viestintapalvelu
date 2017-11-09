package fi.vm.sade.viestintapalvelu.download.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!aws")
public class CacheConfiguration {

    @Bean
    DownloadCache downloadCacheLocal() {
        return new DownloadCacheLocal();
    }
}

@Configuration
@Profile("aws")
class S3CacheConfiguration {

    @Bean
    DownloadCache downloadCacheS3() {
        return new DownloadCacheS3();
    }
}
