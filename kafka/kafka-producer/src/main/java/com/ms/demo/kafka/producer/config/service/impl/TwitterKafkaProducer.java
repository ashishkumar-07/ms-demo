package com.ms.demo.kafka.producer.config.service.impl;

import com.ms.demo.kafka.avro.model.TwitterAvroModel;
import com.ms.demo.kafka.producer.config.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {
    private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;
    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
       log.debug("Sending message {} to topic {}", message.toString(),topicName);
        CompletableFuture<SendResult<Long, TwitterAvroModel>> resultFuture = kafkaTemplate.send(topicName, key, message);
        addCallback(topicName, message, resultFuture);
    }

    private void addCallback(String topicName, TwitterAvroModel message, CompletableFuture<SendResult<Long, TwitterAvroModel>> resultFuture) {
        resultFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Send record metadata. Topic {}, Offset {}, Partition {}, Timestamp {} at time {}",
                        metadata.topic(),
                        metadata.offset(),
                        metadata.partition(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
            else {
                log.error("Error while sending message {} to topic {} <{}>", message, topicName, ex.getMessage());
            }
        });
    }
}
