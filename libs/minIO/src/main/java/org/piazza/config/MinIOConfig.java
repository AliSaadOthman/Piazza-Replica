package org.piazza.config;

import io.minio.MinioClient;
import org.piazza.services.MinIOServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Value("${kafka.minio.url}")
    private String url;
    @Value("${kafka.minio.accessKye}")
    private String accessKey;
    @Value("${kafka.minio.secretKey}")
    private String secretKey;


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build();
    }

}
