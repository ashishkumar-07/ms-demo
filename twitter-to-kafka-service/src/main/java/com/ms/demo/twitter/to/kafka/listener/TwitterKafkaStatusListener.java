package com.ms.demo.twitter.to.kafka.listener;

import com.ms.demo.config.KafkaConfigData;
import com.ms.demo.kafka.avro.model.TwitterAvroModel;
import com.ms.demo.kafka.producer.config.service.KafkaProducer;
import com.ms.demo.twitter.to.kafka.mapper.TwitterStatusToAvroMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
@AllArgsConstructor

public class TwitterKafkaStatusListener extends StatusAdapter {
    private KafkaConfigData kafkaConfigData;
    private KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
    private TwitterStatusToAvroMapper mapper;
    @Override
    public void onStatus(Status status) {
        log.info("Twitter status with text :{}",status.getText());
        TwitterAvroModel twitterModel = mapper.toAvroModel(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), twitterModel.getUserId(), twitterModel);

    }

}
