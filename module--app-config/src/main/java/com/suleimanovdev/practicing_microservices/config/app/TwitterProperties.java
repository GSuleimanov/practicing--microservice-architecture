package com.suleimanovdev.practicing_microservices.config.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "twitter-stream")
public class TwitterProperties {
    private Mock mock;
    private List<String> filterKeywords;

    @Getter
    @Setter
    public static class Mock {
        private boolean enable;
        private int periodBetweenTweets;
    }
}
