package com.ms.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka")
@Data
public class TwitterToKafkaConfigData {
    public List<String> twitterKeywords;
}
