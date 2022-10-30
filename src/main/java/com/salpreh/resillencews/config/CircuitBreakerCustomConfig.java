package com.salpreh.resillencews.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class CircuitBreakerCustomConfig {

    public static final String NATIVE_CB = "native";

    @Bean
    public CircuitBreakerRegistry nativeCircuitBreakerRegistry() {
        var registry = CircuitBreakerRegistry.ofDefaults();
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
