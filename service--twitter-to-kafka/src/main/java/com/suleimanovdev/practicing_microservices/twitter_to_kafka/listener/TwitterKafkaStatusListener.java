package com.suleimanovdev.practicing_microservices.twitter_to_kafka.listener;

import com.suleimanovdev.practicing_microservices.config.KafkaProperties;
import com.suleimanovdev.practicing_microservices.kafka.model.TwitterAvroModel;
import com.suleimanovdev.practicing_microservices.kafka.producer.service.KafkaProducer;
import com.suleimanovdev.practicing_microservices.twitter_to_kafka.transformer.TwitterStatusToAvroTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterKafkaStatusListener extends StatusAdapter {
    private final KafkaProperties props;
    private final KafkaProducer<Long, TwitterAvroModel> producer;
    private final TwitterStatusToAvroTransformer transformer;


    @Override
    public void onStatus(Status status) {
        log.info("Twitter status to topic {} with text: {}", props.getTopicName(), status.getText());
        var avroModel = transformer.getTwitterAvroModelFromStatus(status);
        producer.send(props.getTopicName(), avroModel.getId(), avroModel);
    }
}
