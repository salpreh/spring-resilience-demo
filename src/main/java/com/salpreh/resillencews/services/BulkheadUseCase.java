package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomBulkheadConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

@Log4j2
@Service
@RequiredArgsConstructor
public class BulkheadUseCase {
    private final BulkheadRegistry bhRegistry;
    private final DataCalculatorService dataCalculatorService;

    public TitledData<Integer> nativeBulkhead() {
        Supplier<TitledData<Integer>> bulkheadSupplier = io.github.resilience4j.bulkhead.Bulkhead.decorateSupplier(
            bhRegistry.bulkhead(CustomBulkheadConfig.NATIVE_BH),
            () -> {
                try {
                    log.info("Native Bulkhead task started");
                    var data = dataCalculatorService.executeAsyncWithRandomDelay("BulkheadTask", 6, 12)
                            .get();
                    log.info("Native Bulkhead task executed");

                    return data;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        );

        return Try.of(() ->
                CompletableFuture.supplyAsync(bulkheadSupplier) // Run bulkhead call asynchronously so it can be called easily multiple times
                    .exceptionallyAsync(t -> {
                        log.error("Error computing bulkhead operation", t);
                        return null;
                    })
            )
            .map(__ -> dataCalculatorService.getData("Bulkhead call")) // Always returning something and leaving bulkhead operation running in background
            .recover(dataCalculatorService::recoverFromError)
            .get();

    }

    @Async
    @Bulkhead(name = CustomBulkheadConfig.SPRING_BH, fallbackMethod = "errorFallback")
    public CompletableFuture<TitledData<Integer>> springBulkhead() {
        return dataCalculatorService.executeAsyncWithRandomDelay("Bulkhead call", 6, 12);
    }

    public CompletableFuture<TitledData<Integer>> errorFallback(Throwable t) {
        log.error("Bulkhead fallback", t);
        return CompletableFuture.supplyAsync(() -> dataCalculatorService.recoverFromError(t));
    }
}
