package com.suleimanovdev.practicing_microservices.twitter_to_kafka;

import com.suleimanovdev.practicing_microservices.config.KafkaProperties;
import com.suleimanovdev.practicing_microservices.twitter_to_kafka.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
@ComponentScan(basePackages = "com.suleimanovdev.practicing_microservices")
public class TwitterToKafkaService implements CommandLineRunner {
    private final StreamRunner runner;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("\uD83D\uDD30 Our application succesfully started! \uD83D\uDD30");
        runner.start();
    }
}
