package com.ms.demo.kafka.admin.config;

import com.ms.demo.config.KafkaConfigData;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@EnableRetry
@Configuration
@AllArgsConstructor
public class KafkaAdminConfig {
    private KafkaConfigData kafkaConfigData;

    @Bean
    public AdminClient adminCLient(){
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootStrapServers()));
    }
}
