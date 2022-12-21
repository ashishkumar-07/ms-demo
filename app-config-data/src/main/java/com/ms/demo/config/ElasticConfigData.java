package com.ms.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elastic-config")
@Data
public class ElasticConfigData {
    private String indexName;
    private String connectionUrl;
    private String connectionTimeoutMs;
    private String socketTimeOutMs;
    private Boolean isRepository;
}
