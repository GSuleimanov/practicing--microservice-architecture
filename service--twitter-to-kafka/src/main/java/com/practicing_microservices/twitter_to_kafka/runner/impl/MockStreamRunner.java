package com.practicing_microservices.twitter_to_kafka.runner.impl;

import com.practicing_microservices.twitter_to_kafka.config.AppConfig;
import com.practicing_microservices.twitter_to_kafka.exception.TwitterToKafkaException;
import com.practicing_microservices.twitter_to_kafka.listener.TwitterKafkaStatusListener;
import com.practicing_microservices.twitter_to_kafka.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${twitter-stream.mock.enable}")
public class MockStreamRunner implements StreamRunner {
    private final AppConfig config;
    private final TwitterKafkaStatusListener listener;

    private final static int MIN_TWEET_LENGTH = 3;
    private final static int MAX_TWEET_LENGTH = 20;
    private final static String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    private static final Random RANDOM = new Random();
    private static final String[] WORDS = new String[]{
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "elit", "sed",
            "adipiscing", "do", "eiusmod", "tempor", "incididunt", "ut", "labore",
            "et", "dolore", "magna", "aliqua", "ut", "enim", "ad", "minim", "veniam"
    };

    private static final String tweetAsRawJson = """
            {
                "createdAt":"%s",
                "id":%s,
                "text":"%s",
                "user":{"id": %s}
            }
            """;

    @Override
    public void start() throws TwitterException {
        log.info("Twitter is mocked!");
        int delayBetweenTweets = config.getTwitterStream().getMock().getPeriodBetweenTweets();
        log.info("Time between tweets is set to {} ms", delayBetweenTweets);
        simulateTweeterStream(delayBetweenTweets);
    }

    private void simulateTweeterStream(int delayBetweenTweets) throws TwitterException {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                String formattedTweetAsRawJson = substituteTweetAndGet();
                Status status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson);
                listener.onStatus(status);
                sleep(delayBetweenTweets);
            }
        });
    }

    private void sleep(int delayBetweenTweets) {
        try {
            Thread.sleep(delayBetweenTweets);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaException("Error while waiting for next status!");
        }
    }

    private String substituteTweetAndGet() {
        String[] params = new String[]{
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                generateTweetText(),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };
        return format(tweetAsRawJson, (Object[]) params);
    }

    private String generateTweetText() {
        StringBuilder tweet = new StringBuilder();
        IntConsumer appendWord = x -> tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
        int numberOfWordsInTweet = RANDOM.nextInt(MIN_TWEET_LENGTH, MAX_TWEET_LENGTH);
        IntStream.range(MIN_TWEET_LENGTH, numberOfWordsInTweet).forEach(appendWord);
        return tweet.toString().trim();
    }
}
