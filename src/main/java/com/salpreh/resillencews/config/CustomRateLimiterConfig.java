package com.salpreh.resillencews.config;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
public class CustomRateLimiterConfig {

    public static final String NATIVE_RL = "native";
    public static final String SPRING_RL = "spring";

    private final RateLimiterRegistry registry;

    @PostConstruct
    public RateLimiterRegistry registerConfigs() {
        addNativeRateLimiter(registry);

        return registry;
    }

    private void addNativeRateLimiter(RateLimiterRegistry registry) {
        RateLimiterConfig config = RateLimiterConfig.custom()
            .timeoutDuration(Duration.of(5, ChronoUnit.SECONDS))
            .limitRefreshPeriod(Duration.of(10, ChronoUnit.SECONDS))
            .limitForPeriod(4)
            .build();

        registry.rateLimiter(NATIVE_RL, config);
    }
}
