package com.ms.demo.kafka.to.elastic.exception;

public class KafkaToElasticException extends RuntimeException{
    public KafkaToElasticException(){
        super();
    }

    public KafkaToElasticException(String message){
        super(message);
    }

    public KafkaToElasticException(String message, Throwable cause){
        super(message, cause);
    }
}
