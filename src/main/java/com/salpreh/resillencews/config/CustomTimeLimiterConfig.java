package com.salpreh.resillencews.config;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class CustomTimeLimiterConfig {

    public static final String NATIVE_TL = "native";
    public static final String SPRING_TL = "spring";
    private final TimeLimiterRegistry registry;

    @PostConstruct
    public TimeLimiterRegistry registerConfig() {
        addNativeRateLimiter(registry);

        return registry;
    }

    private void addNativeRateLimiter(TimeLimiterRegistry registry) {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
            .cancelRunningFuture(true)
            .timeoutDuration(Duration.ofMillis(5000))
            .build();

        registry.timeLimiter(NATIVE_TL, config);
    }

}
