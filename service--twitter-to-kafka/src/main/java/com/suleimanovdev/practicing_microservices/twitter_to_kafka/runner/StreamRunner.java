package com.suleimanovdev.practicing_microservices.twitter_to_kafka.runner;

import twitter4j.TwitterException;

public interface StreamRunner {
    void start() throws TwitterException;
}
