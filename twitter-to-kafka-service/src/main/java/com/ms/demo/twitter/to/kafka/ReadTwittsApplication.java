package com.ms.demo.twitter.to.kafka;

import com.ms.demo.config.TwitterToKafkaConfigData;
import com.ms.demo.twitter.to.kafka.runner.StreamRunner;
import com.ms.demo.twitter.to.kafka.service.StreamInitializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
@ComponentScan(basePackages = "com.ms.demo")
public class ReadTwittsApplication implements CommandLineRunner {

    private StreamRunner runner;
    private StreamInitializer initializer;

    public static void main(String[] args) {
        SpringApplication.run(ReadTwittsApplication.class);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("Welcome to twitter-to-kafka app");
        initializer.init();
        runner.start();
    }
}
