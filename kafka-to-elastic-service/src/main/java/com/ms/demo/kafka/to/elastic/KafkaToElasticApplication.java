package com.ms.demo.kafka.to.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ComponentScan(basePackages = "com.ms.demo")
public class KafkaToElasticApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaToElasticApplication.class);
    }
}
