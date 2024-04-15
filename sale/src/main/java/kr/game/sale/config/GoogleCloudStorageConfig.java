package kr.game.sale.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudStorageConfig {
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;
    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;
    @Bean
    public Storage storage() throws IOException{
        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream((keyFile)))
                .build()
                .getService();
    }
}
