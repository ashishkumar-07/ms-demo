package com.ms.demo.kafka.admin.client;

import com.ms.demo.config.KafkaConfigData;
import com.ms.demo.config.RetryConfigData;
import com.ms.demo.kafka.admin.exception.KafkaClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaAdminClient {
    private KafkaConfigData kafkaConfigData;
    private RetryConfigData retryConfigData;
    private RetryTemplate retryTemplate;
    private AdminClient adminClient;
    private WebClient webClient;

    public void createTopics(){
        CreateTopicsResult createTopicsResult;
        try{
            createTopicsResult = retryTemplate.execute(this::doCreateTopic);
        }
        catch (Throwable t){
            throw new KafkaClientException("Reached max no. of retries for creating kafka topic(s)!");
        }
        checkTopicCreated();
    }

    private CreateTopicsResult doCreateTopic(RetryContext retryContext) {
        List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating {} topic(s) , attempts {}", topicNames.size(), retryContext.getRetryCount());
        List<NewTopic> newTopics = topicNames.stream()
                .map(topic -> new NewTopic(topic.trim(), kafkaConfigData.getNumOfPartitions(), kafkaConfigData.getReplicationFactor()))
                .toList();
        return adminClient.createTopics(newTopics);
    }
    public void checkTopicCreated(){
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetryCount = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
                while (!isTopicCreated(topics,topic)){
                    checkMaxRetry(retryCount++, maxRetryCount);
                    sleep(sleepTimeMs);
                    sleepTimeMs = sleepTimeMs*multiplier;
                    topics = getTopics();
                }

        }
    }

    public void checkSchemaRegistryUpAndRunning(){
        int retryCount = 1;
        Integer maxRetryCount = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while(!getSchemaRegistryStatus().is2xxSuccessful()){
            checkMaxRetry(retryCount++, maxRetryCount);
            sleep(sleepTimeMs);
            sleepTimeMs = sleepTimeMs*multiplier;
        }
    }

    private HttpStatusCode getSchemaRegistryStatus(){
        try {
            HttpStatusCode statusCode = webClient.method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchangeToMono(clientResponse -> clientResponse.toBodilessEntity())
                    .map(b -> b.getStatusCode())
                    .block();
            return statusCode;
        }
        catch (Exception e){
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }
    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if(Objects.isNull(topics)){
            return false;
        }
        return topics.stream().anyMatch(topic->topic.name().equals(topicName));
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if(retry>maxRetry){
            throw new KafkaClientException("Reached max no. of retries for readings topic(s)!");
        }
    }

    private Collection<TopicListing> getTopics(){
        Collection<TopicListing> topics;
        try{
            topics = retryTemplate.execute(this::doGetTopics);
            return topics;
        }
        catch (Throwable t){
            new KafkaClientException("Reached max no. of retries for readings topic(s)!", t);
        }

        return null;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        log.info("Reading kafka topic {}, attempt {}", kafkaConfigData.getTopicNamesToCreate().toArray(), retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if(Objects.nonNull(topics)){
            topics.forEach(topic ->{
                log.info("Topic with name {}", topic);
            });
        }
        return topics;
    }


    private void sleep(Long sleepTimeInMs)  {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepTimeInMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping when fetching the topics!", e);
        }
    }

    private void retryCheck(Integer retry, Integer maxRetry){
        if(retry>maxRetry){
            throw new KafkaClientException("Max retry reached!");
        }
    }

}
