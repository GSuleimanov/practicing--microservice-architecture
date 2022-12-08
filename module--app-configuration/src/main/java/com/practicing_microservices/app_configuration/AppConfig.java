package com.practicing_microservices.app_configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Slf4j
@Configuration
@ConfigurationProperties
public class AppConfig {
    private String welcomeMessage;
    private TwitterConfig twitterStream;

    @Data
    public static class TwitterConfig {
        private TwitterMock mock;
        private List<String> filterKeywords;

        @Data
        public static class TwitterMock {
            private boolean enable;
            private int periodBetweenTweets;
        }
    }
}
