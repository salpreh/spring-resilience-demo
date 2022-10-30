package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomRateLimiterConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
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
        Supplier<TitledData<Integer>> rlSupplier = RateLimiter.decorateSupplier(
            rlRegistry.rateLimiter(CustomRateLimiterConfig.NATIVE_RL),
            () -> {
                log.info("Rate limited call");
                return dataCalculatorService.getData("RateLimiter call");
            }
        );

        Try<TitledData<Integer>> recoverSupplier = Try.ofSupplier(rlSupplier)
            .recover(t -> {
                log.warn("Request rate exceeded in RL");
                return dataCalculatorService.recoverFromError(t);
            });

        return recoverSupplier.get();
    }
}
