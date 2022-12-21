package com.ms.demo.twitter.to.kafka.service.impl;

import com.ms.demo.config.KafkaConfigData;
import com.ms.demo.kafka.admin.client.KafkaAdminClient;
import com.ms.demo.twitter.to.kafka.service.StreamInitializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class TwitterStreamIntitializer implements StreamInitializer {
    private KafkaAdminClient adminClient;
    private KafkaConfigData kafkaConfigData;
    @Override
    public void init() {
        adminClient.createTopics();
        adminClient.checkSchemaRegistryUpAndRunning();
        log.info("Topics {} are ready for operations.", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
