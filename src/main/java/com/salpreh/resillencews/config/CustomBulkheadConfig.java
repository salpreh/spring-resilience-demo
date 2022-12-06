package com.salpreh.resillencews.config;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.bulkhead.configure.threadpool.ThreadPoolBulkheadConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class CustomBulkheadConfig {

    public static final String NATIVE_BH = "native";
    public static final String SPRING_BH = "spring";

    private final BulkheadRegistry registry;
    private final ThreadPoolBulkheadRegistry poolRegistry;

    @PostConstruct
    public void registerConfigs() {
        addNativeBulkhead(registry);
        addNativePoolBulkhead(poolRegistry);
    }

    public void addNativeBulkhead(BulkheadRegistry registry) {
        BulkheadConfig config = BulkheadConfig.custom()
            .maxConcurrentCalls(4)
            .maxWaitDuration(Duration.ofMillis(4000))
            .build();

        registry.bulkhead(NATIVE_BH, config);
    }

    public void addNativePoolBulkhead(ThreadPoolBulkheadRegistry registry) {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
            .coreThreadPoolSize(2)
            .maxThreadPoolSize(4)
            .queueCapacity(10)
            .keepAliveDuration(Duration.ofSeconds(3))
            .build();

        registry.bulkhead(NATIVE_BH, config);
    }
}
