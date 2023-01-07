package com.suleimanovdev.practicing_microservices.kafka.admin.client;

import com.suleimanovdev.practicing_microservices.config.app.KafkaProperties;
import com.suleimanovdev.practicing_microservices.config.app.RetryProperties;
import com.suleimanovdev.practicing_microservices.kafka.admin.exception.KafkaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAdminClient {
    private final KafkaProperties kafkaProps;
    private final RetryProperties retryProps;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public void createTopic() {
        try {
            var result = retryTemplate.execute(this::doCreateTopics);
        } catch (Throwable t) {
            throw new KafkaClientException("Reached max number of retries while trying to create kafka topics!");
        }
        checkTopicCreated();
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        var topicNames = kafkaProps.getTopicNamesToCreate();
        log.info("Creating {} topicNames, attempt {}", topicNames.size(), retryContext.getRetryCount());
        var topics = topicNames.stream().map(topicName -> new NewTopic(
                topicName,
                kafkaProps.getNumberOfPartitions(),
                kafkaProps.getReplicationFactor()
        )).toList();
        return adminClient.createTopics(topics);
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            throw new KafkaClientException("Reached max number of retries for reading kafka topics!", t);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        log.info("Reading {} topicNames, attempt {}", kafkaProps.getTopicNamesToCreate(), retryContext.getRetryCount());
        var topics = adminClient.listTopics().listings().get();
        Optional.ofNullable(topics).ifPresent(ts ->
                ts.forEach(t -> log.info("Topic with name {}", t.name())));
        return topics;
    }

    public void checkTopicCreated() {
        var topics = getTopics();
        int retryCount = 1;
        int multiplier = retryProps.getMultiplier().intValue();
        Integer maxRetry = retryProps.getMaxAttempts();
        Long sleepTimeMs = retryProps.getSleepTimeMs();
        for (String topicName : kafkaProps.getTopicNamesToCreate())
            while (!isTopicCreated(topics, topicName)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
                topics = getTopics();
            }
    }

    public void checkSchemaRegistry() {
        int retryCount = 1;
        int multiplier = retryProps.getMultiplier().intValue();
        Integer maxRetry = retryProps.getMaxAttempts();
        Long sleepTimeMs = retryProps.getSleepTimeMs();
        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        return webClient
                .method(HttpMethod.GET)
                .uri(kafkaProps.getSchemaRegistryUrl())
                .retrieve()
                .bodyToMono(HttpStatus.class)
                .block();
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping for waiting new created topics!" ,e);
        }
    }

    private void checkMaxRetry(int counter, Integer maxRetry) {
        if (counter > maxRetry)
            throw new KafkaClientException("Reached maximum number of retry for reading kafka topics!");
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        return topics != null && topics.stream().anyMatch(t -> t.name().equals(topicName));
    }
}
