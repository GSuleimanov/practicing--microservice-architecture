package com.suleimanovdev.practicing_microservices.config.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-stream")
public class TwitterConfig {
    private Mock mock;
    private List<String> filterKeywords;

    @Data
    public static class Mock {
        private final boolean enable;
        private final int periodBetweenTweets;
    }
}
