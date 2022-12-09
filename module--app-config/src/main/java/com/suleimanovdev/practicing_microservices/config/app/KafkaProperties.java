package com.suleimanovdev.practicing_microservices.config.app;

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

    @Getter
    @Setter
    private static class Producer {
        private String keySerializerClass;
        private String valueSerializerClass;
        private String compressionType;
        private String acks;
        private Integer batchSize;
        private Integer batchSizeBoostFactor;
        private Integer lingerMs;
        private Integer requestTimeoutMs;
        private Integer retryCount;
    }
}