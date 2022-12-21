package com.ms.demo.kafka.to.elastic.consumer.impl;

import com.ms.demo.config.KafkaConfigData;
import com.ms.demo.config.KafkaConsumerConfigData;
import com.ms.demo.elastic.index.client.service.ElasticIndexClient;
import com.ms.demo.elastic.model.impl.TwitterIndexModel;
import com.ms.demo.kafka.admin.client.KafkaAdminClient;
import com.ms.demo.kafka.avro.model.TwitterAvroModel;
import com.ms.demo.kafka.to.elastic.consumer.KafkaConsumer;
import com.ms.demo.kafka.to.elastic.consumer.mapper.AvroToElasticMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private KafkaAdminClient kafkaAdminClient;
    private KafkaConfigData kafkaConfigData;

    private KafkaConsumerConfigData kafkaConsumerConfigData;
    private ElasticIndexClient elasticIndexClient;

    private AvroToElasticMapper avroToElasticMapper;


    @EventListener
    public void onStartUp(ApplicationReadyEvent applicationReadyEvent){
        kafkaAdminClient.checkTopicCreated();
        log.info("Topics with name {} are ready for operations", kafkaConfigData.getTopicName());
        kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId()).start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumerGroupId}", topics="${kafka-config.topicName}", groupId ="${kafka-consumer-config.consumerGroupId}")
    public void receive(@Payload List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offset) {
        log.info("{} number of messages received with keys {}, partitions {} and offsets {}, sending it to elastic: Thread Id: {}",
                messages.size(),
                keys,
                partitions,
                offset,
                Thread.currentThread().getId());
        List<TwitterIndexModel> twitterIndexModels = avroToElasticMapper.toElasticTwitterModel(messages);
        try {
            elasticIndexClient.save(twitterIndexModels);
        } catch (IOException e) {
            log.info("Failed to save twitts {} in elastic cluster", messages);
            throw new RuntimeException("Failed to save twitts in elastic cluster!");
        }
    }
}
