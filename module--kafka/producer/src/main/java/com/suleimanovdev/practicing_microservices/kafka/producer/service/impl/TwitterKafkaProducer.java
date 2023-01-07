package com.suleimanovdev.practicing_microservices.kafka.producer.service.impl;

import com.suleimanovdev.practicing_microservices.kafka.model.TwitterAvroModel;
import com.suleimanovdev.practicing_microservices.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {
    private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    @Override
    public void send(String topicName, Long key, TwitterAvroModel value) {
        log.info("Sending message {} to topic {}", value.getText(), topicName);
        kafkaTemplate.send(topicName, key, value)
                .completable()
                .thenAccept(logSuccess())
                .exceptionally(logError(topicName));
    }

    @PreDestroy
    public void close() {
        Optional.ofNullable(kafkaTemplate).ifPresent(kt -> {
            log.info("Closing kafka producer!");
            kt.destroy();
        });
    }

    private static Consumer<SendResult<Long, TwitterAvroModel>> logSuccess() {
        return r -> {
            var meta = r.getRecordMetadata();
            log.info("Received new metadata. Topic {}; Partition {}; Offset {}; Timestamp {}; at time {}",
                    meta.topic(), meta.partition(), meta.offset(), meta.timestamp(), System.nanoTime());
        };
    }

    private static Function<Throwable, Void> logError(String topicName) {
        return r -> {
            log.error("Error while sending message {} to topic {}", r.toString(), topicName);
            return null;
        };
    }
}
