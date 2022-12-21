package com.ms.demo.elastic.query.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ms.demo")
public class ElasticQueryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryApplication.class);
    }
}
