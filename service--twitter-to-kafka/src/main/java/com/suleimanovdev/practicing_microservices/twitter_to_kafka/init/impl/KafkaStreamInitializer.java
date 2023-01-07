package com.suleimanovdev.practicing_microservices.twitter_to_kafka.init.impl;

import com.suleimanovdev.practicing_microservices.config.KafkaProperties;
import com.suleimanovdev.practicing_microservices.kafka.admin.client.KafkaAdminClient;
import com.suleimanovdev.practicing_microservices.twitter_to_kafka.init.StreamInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStreamInitializer implements StreamInitializer {
    private final KafkaAdminClient admin;
    private final KafkaProperties props;

    @Override
    public void init() {
        admin.createTopic();
        admin.checkSchemaRegistry();
        log.info("Topics with name {} are successfully initialized and ready!", props.getTopicNamesToCreate());
    }
}
