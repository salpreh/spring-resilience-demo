package com.salpreh.resillencews.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
public class CustomCircuitBreakerConfig {

    public static final String NATIVE_CB = "native";
    public static final String SPRING_CB = "spring";

    private final CircuitBreakerRegistry registry;

    @PostConstruct
    public CircuitBreakerRegistry registerConfigs() {
        addNativeCircuitBreaker(registry);

        return registry;
    }

    private void addNativeCircuitBreaker(CircuitBreakerRegistry registry) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .failureRateThreshold(50) // failure threshold in percentage
            .waitDurationInOpenState(Duration.of(5, ChronoUnit.SECONDS))
            .permittedNumberOfCallsInHalfOpenState(2) // permitted calls in half-open state
            .slidingWindowSize(6) // CB sliding window size
            .minimumNumberOfCalls(3) // minimum CB sliding window size for evaluate failure rate
            .build();

        registry.circuitBreaker(NATIVE_CB, config);
    }
}
