package com.suleimanovdev.practicing_microservices.twitter_to_kafka.runner.impl;

import com.suleimanovdev.practicing_microservices.config.TwitterProperties;
import com.suleimanovdev.practicing_microservices.twitter_to_kafka.listener.TwitterKafkaStatusListener;
import com.suleimanovdev.practicing_microservices.twitter_to_kafka.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("not ${twitter-stream.mock.enable}")
public class TwitterKafkaStreamRunner implements StreamRunner {
    final private TwitterProperties twitterProps;
    final private TwitterKafkaStatusListener listener;
    private TwitterStream stream;

    @Override
    public void start() {
        stream = new TwitterStreamFactory().getInstance();
        stream.addListener(listener);
        stream.filter(createFilterQuery());
    }

    @PreDestroy
    private void shutdown() {
        Optional.ofNullable(stream).ifPresent(s -> {
            log.info("Closing twitter stream before shutdown!");
            s.shutdown();
        });
    }

    public FilterQuery createFilterQuery() {
        var kwords = twitterProps.getFilterKeywords().toArray(new String[0]);
        log.info("Created filter from resources for next keywords: {}", (Object[]) kwords);
        return new FilterQuery(kwords);
    }
}
