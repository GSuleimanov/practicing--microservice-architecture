package com.suleimanovdev.practicing_microservices.kafka.admin.config;

import com.suleimanovdev.practicing_microservices.config.app.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@EnableRetry
@Configuration
@RequiredArgsConstructor
public class KafkaAdminConfig {
    private final KafkaProperties kafkaProps;

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(Map.of(
                CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers()));
    }
}
