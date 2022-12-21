package com.ms.demo.twitter.to.kafka.runner.impl;

import com.ms.demo.config.TwitterToKafkaConfigData;
import com.ms.demo.twitter.to.kafka.listener.TwitterKafkaStatusListener;
import com.ms.demo.twitter.to.kafka.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Arrays;

@Component
@Slf4j
public class TwitterStreamRunner implements StreamRunner {
    private TwitterToKafkaConfigData twitterToKafkaConfig;

    public TwitterStreamRunner(TwitterToKafkaConfigData twitterToKafkaConfig, TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterToKafkaConfig = twitterToKafkaConfig;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    private TwitterKafkaStatusListener twitterKafkaStatusListener;
    private TwitterStream twitterStream;

    @PreDestroy
    public void shutdown(){
        if (twitterStream != null) {
            log.info("Closing the twitter stream");
            twitterStream.shutdown();
        }
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = TwitterStreamFactory.getSingleton();
        twitterStream.addListener(twitterKafkaStatusListener);
        String[] keywords = twitterToKafkaConfig.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        log.info("Started filtering for keywords : {}", Arrays.toString(keywords));

    }
}
