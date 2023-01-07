package com.suleimanovdev.practicing_microservices.commons.config;

import com.suleimanovdev.practicing_microservices.config.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfig {
    private final KafkaProperties kafkaProps;

    @Bean
    public RetryTemplate retryTemplate() {
        var retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(generateExponentialBackOffPolicy());
        retryTemplate.setRetryPolicy(generateSimpleRetryPolicy());
        return retryTemplate;
    }

    private SimpleRetryPolicy generateSimpleRetryPolicy() {
        return new SimpleRetryPolicy(kafkaProps.getRetry().maxAttempts());
    }

    private ExponentialBackOffPolicy generateExponentialBackOffPolicy() {
        var p = new ExponentialBackOffPolicy();
        p.setInitialInterval(kafkaProps.getRetry().initialIntervalMs());
        p.setMaxInterval(kafkaProps.getRetry().maxIntervalMs());
        p.setMultiplier(kafkaProps.getRetry().multiplier());
        return p;
    }
}
