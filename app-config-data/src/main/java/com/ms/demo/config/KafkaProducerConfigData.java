package com.ms.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
public class KafkaProducerConfigData {
    String keySerializerClass;
    String valueSerializerClass;
    String compressionType;
    String acks;
    Integer bachSize;
    Integer bachSizeBoostFactor;
    Integer lingerMs;
    Integer requestTimeoutMs;
    Integer retryCount;
}
