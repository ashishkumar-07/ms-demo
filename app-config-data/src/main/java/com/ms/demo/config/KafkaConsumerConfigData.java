package com.ms.demo.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {
    String keyDeserializer;
    String valueDeserializer;
    String consumerGroupId;
    String autoOffsetReset;
    String specificAvroReaderKey;
    String specificAvroReader;
    Boolean batchListener;
    Boolean autoStartup;
    Integer concurrencyModel;
    Integer sessionTimeoutMs;
    Integer heartbeatIntervalMs;
    Integer maxPollIntervalMs;
    Integer maxPollRecords;
    Integer maxPartitionFetchBytesDefault;
    Integer maxPartitionFetchBytesBoostFactor;
    Long pollTimeoutMs;
}
