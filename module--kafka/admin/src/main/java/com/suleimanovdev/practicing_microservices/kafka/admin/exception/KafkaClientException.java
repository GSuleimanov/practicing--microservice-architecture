package com.suleimanovdev.practicing_microservices.kafka.admin.exception;

public class KafkaClientException extends RuntimeException {
    public KafkaClientException() {
    }

    public KafkaClientException(String s) {
    }

    public KafkaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
