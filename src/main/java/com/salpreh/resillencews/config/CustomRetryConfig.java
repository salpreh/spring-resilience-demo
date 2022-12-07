package com.salpreh.resillencews.config;

import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class CustomRetryConfig {

    public static final String NATIVE_RT = "native";
    public static final String SPRING_RT = "spring";
    private final RetryRegistry registry;

    @PostConstruct
    public void registerConfig() {

    }

    private void addNativeRetry(RetryRegistry registry) {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(Duration.ofMillis(200))
            .failAfterMaxAttempts(true)
            .build();

        registry.retry(NATIVE_RT, config);
    }
}
