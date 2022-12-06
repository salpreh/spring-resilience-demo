package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomTimeLimiterConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Log4j2
public class TimeLimiterUseCase {
    private final TimeLimiterRegistry tlRegistry;
    private final DataCalculatorService dataCalculatorService;
    private final Random random = new Random();

    public TitledData<Integer> nativeTimeLimiter() {
        return Try.ofSupplier(() -> {
                try {
                    return tlRegistry.timeLimiter(CustomTimeLimiterConfig.NATIVE_TL)
                        .executeFutureSupplier(() ->
                            executeAsyncWithRandomDelay(() -> dataCalculatorService.getData("TimeLimiter call"), 2, 7)
                        );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .recover(t -> {
                log.warn("Time limit exceeded  in native TL");
                return dataCalculatorService.recoverFromError(t);
            })
            .get();
    }

    private <T> CompletableFuture<T> executeAsyncWithRandomDelay(Supplier<T> supplier, int minSeconds, int maxSeconds) {
        int delaySecs = random.nextInt(maxSeconds - minSeconds + 1) + minSeconds;

        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Waiting {} seconds", delaySecs);
                TimeUnit.SECONDS.sleep(delaySecs);

                return supplier.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
