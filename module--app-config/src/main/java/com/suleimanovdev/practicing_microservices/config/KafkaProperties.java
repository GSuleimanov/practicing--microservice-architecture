package com.suleimanovdev.practicing_microservices.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private String topicName;
    private List<String> topicNamesToCreate;
    private Integer numberOfPartitions;
    private Short replicationFactor;
    private ProducerProperties producer;
    private RetryProperties retry;

    public record ProducerProperties(
            String keySerializerClass,
            String valueSerializerClass,
            String compressionType,
            String acks,
            Integer batchSize,
            Integer batchSizeBoostFactor,
            Integer lingerMs,
            Integer requestTimeoutMs,
            Integer retryCount) {}

    public record RetryProperties(
        Long initialIntervalMs,
        Long maxIntervalMs,
        Double multiplier,
        Integer maxAttempts,
        Long sleepTimeMs) {}
}