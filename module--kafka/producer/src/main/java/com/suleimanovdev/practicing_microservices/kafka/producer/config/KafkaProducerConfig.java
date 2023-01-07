package com.suleimanovdev.practicing_microservices.kafka.producer.config;

import com.suleimanovdev.practicing_microservices.config.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {
    private final KafkaProperties kafkaProps;

    @Bean
    public Map<String, Object> producerConfig() {
        var producerProps = kafkaProps.getProducer();
        return new HashMap<>() {{
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers());
            put(kafkaProps.getSchemaRegistryUrlKey(), kafkaProps.getSchemaRegistryUrl());
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProps.keySerializerClass());
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerProps.valueSerializerClass());
            put(ProducerConfig.BATCH_SIZE_CONFIG, producerProps.batchSize() * producerProps.batchSizeBoostFactor());
            put(ProducerConfig.LINGER_MS_CONFIG, producerProps.lingerMs());
            put(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerProps.compressionType());
            put(ProducerConfig.ACKS_CONFIG, producerProps.acks());
            put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProps.requestTimeoutMs());
            put(ProducerConfig.RETRIES_CONFIG, producerProps.retryCount());
        }};
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
