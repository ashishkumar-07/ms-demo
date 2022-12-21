package com.ms.demo.kafka.consumer.config;

import com.ms.demo.config.KafkaConfigData;
import com.ms.demo.config.KafkaConsumerConfigData;
import lombok.AllArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@AllArgsConstructor
public class KafkaConsumerConfig <K extends Serializable, V extends SpecificRecordBase> {
    private KafkaConfigData kafkaConfigData;
    private KafkaConsumerConfigData kafkaConsumerConfigData;

    public Map<String,Object> consumerConfig(){

        Map<String , Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootStrapServers());
        configs.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        configs.put(kafkaConsumerConfigData.getSpecificAvroReaderKey(), kafkaConsumerConfigData.getSpecificAvroReader());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getKeyDeserializer());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getValueDeserializer());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfigData.getConsumerGroupId());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigData.getSessionTimeoutMs());
        configs.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getHeartbeatIntervalMs());
        configs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getMaxPollIntervalMs());
        configs.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafkaConsumerConfigData.getMaxPartitionFetchBytesDefault()
                * kafkaConsumerConfigData.getMaxPartitionFetchBytesBoostFactor());
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigData.getMaxPollRecords());
        return configs;
    }

    @Bean
    public ConsumerFactory<K,V> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K,V>> listenerContainerFactory(ConsumerFactory<K,V> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<K,V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(kafkaConsumerConfigData.getConcurrencyModel());
        factory.setBatchListener(kafkaConsumerConfigData.getBatchListener());
        factory.setAutoStartup(kafkaConsumerConfigData.getAutoStartup());
        factory.getContainerProperties().setPollTimeout(kafkaConsumerConfigData.getPollTimeoutMs());
        return factory;
    }


}
