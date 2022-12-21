package com.ms.demo.common.config;

import com.ms.demo.config.RetryConfigData;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@AllArgsConstructor
public class RetryConfig {
    private RetryConfigData retryConfigData;

    @Bean
    public RetryTemplate retryTemplate(){
        RetryTemplate rt = new RetryTemplate();
        ExponentialBackOffPolicy ep = new ExponentialBackOffPolicy();
        ep.setInitialInterval(retryConfigData.getInitialIntervalMs());
        ep.setMaxInterval(retryConfigData.getMaxIntervalMs());
        ep.setMultiplier(retryConfigData.getMultiplier());

        rt.setBackOffPolicy(ep);
        SimpleRetryPolicy srp = new SimpleRetryPolicy();
        srp.setMaxAttempts(retryConfigData.getMaxAttempts());
        rt.setRetryPolicy(srp);
        return rt;
    }
}
