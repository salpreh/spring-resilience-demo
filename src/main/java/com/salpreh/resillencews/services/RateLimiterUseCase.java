package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomRateLimiterConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Log4j2
public class RateLimiterUseCase {
    private final RateLimiterRegistry rlRegistry;
    private final DataCalculatorService dataCalculatorService;

    public TitledData<Integer> nativeRateLimiter() {
        Supplier<TitledData<Integer>> rlSupplier = io.github.resilience4j.ratelimiter.RateLimiter.decorateSupplier(
            rlRegistry.rateLimiter(CustomRateLimiterConfig.NATIVE_RL),
            () -> {
                log.info("Native rate limited call");
                return dataCalculatorService.getData("RateLimiter call");
            }
        );

        Try<TitledData<Integer>> recoverSupplier = Try.ofSupplier(rlSupplier)
            .recover(t -> {
                log.warn("Request rate exceeded in native RL");
                return dataCalculatorService.recoverFromError(t);
            });

        return recoverSupplier.get();
    }

    @RateLimiter(name = CustomRateLimiterConfig.SPRING_RL, fallbackMethod = "errorFallback")
    public TitledData<Integer> springRateLimiter() {
        log.info("Spring rate limited call");

        return dataCalculatorService.getData("RateLimiter call");
    }

    private TitledData<Integer> errorFallback(Throwable t) {
        log.warn("Request rate exceeded in spring RL", t);
        return dataCalculatorService.recoverFromError(t);
    }
}
