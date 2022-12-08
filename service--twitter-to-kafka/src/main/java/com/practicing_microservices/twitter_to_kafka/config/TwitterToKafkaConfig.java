package com.practicing_microservices.twitter_to_kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix="twitter-filter")
public class TwitterToKafkaConfig {
    private List<String> keywords;
    private String welcomeMessage;
}
