package com.ms.demo.twitter.to.kafka.mapper;

import com.ms.demo.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterStatusToAvroMapper {

    public TwitterAvroModel toAvroModel(Status status){
        return TwitterAvroModel.newBuilder()
                .setCreatedAt(status.getCreatedAt().getTime())
                .setText(status.getText())
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .build();
    }
}
