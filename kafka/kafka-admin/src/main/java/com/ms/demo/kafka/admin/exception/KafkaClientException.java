package com.ms.demo.kafka.admin.exception;

import com.ms.demo.kafka.admin.client.KafkaAdminClient;

public class KafkaClientException extends RuntimeException{
    public KafkaClientException(){
        super();
    }
    public KafkaClientException(String message){
        super(message);
    }
    public KafkaClientException(String message,Throwable t){
        super(message,t);
    }
}
