package com.ms.demo.twitter.to.kafka.runner;

import twitter4j.TwitterException;

public interface StreamRunner {
    public void start() throws TwitterException;
}
