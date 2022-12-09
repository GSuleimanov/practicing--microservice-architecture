package com.suleimanovdev.practicing_microservices.config.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryProperties {
    private Long initialIntervalMs;
    private Long maxIntervalMs;
    private Double multiplier;
    private Integer maxAttempts;
    private Long sleepTimeMs;
}