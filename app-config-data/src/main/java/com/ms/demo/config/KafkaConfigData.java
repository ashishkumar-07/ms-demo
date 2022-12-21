package com.ms.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {
    String bootStrapServers;
    String schemaRegistryUrlKey;
    String schemaRegistryUrl;
    String topicName;
    List<String> topicNamesToCreate;
    Integer numOfPartitions;
    Short replicationFactor;
}
