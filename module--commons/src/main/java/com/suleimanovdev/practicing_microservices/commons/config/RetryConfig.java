package com.suleimanovdev.practicing_microservices.commons.config;

import com.suleimanovdev.practicing_microservices.config.KafkaProperties;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Data
@Configuration
public class RetryConfig {
    private final KafkaProperties.RetryProperties retryProps;

    @Bean
    public RetryTemplate retryTemplate() {
        var retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(generateExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(generateSimpleRetryPolicy());
        return retryTemplate;
    }

    private SimpleRetryPolicy generateSimpleRetryPolicy() {
        return new SimpleRetryPolicy(retryProps.maxAttempts());
    }

    private ExponentialBackOffPolicy generateExponentialBackOffPolicy() {
        var p = new ExponentialBackOffPolicy();
        p.setInitialInterval(retryProps.initialIntervalMs());
        p.setMaxInterval(retryProps.maxIntervalMs());
        p.setMultiplier(retryProps.multiplier());
        return p;
    }
}
