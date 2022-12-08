package com.practicing_microservices.twitter_to_kafka;

import com.practicing_microservices.twitter_to_kafka.config.TwitterToKafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class TwitterToKafkaService implements CommandLineRunner {
    final private TwitterToKafkaConfig config;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("\uD83D\uDD30 Our application succesfully started! \uD83D\uDD30");
        log.info(config.getWelcomeMessage());
        log.info("config = {}", config.getKeywords());
    }
}
