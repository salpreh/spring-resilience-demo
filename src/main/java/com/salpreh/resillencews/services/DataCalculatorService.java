package com.salpreh.resillencews.services;

import com.salpreh.resillencews.models.TitledData;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Log4j2
@Service
public class DataCalculatorService {

    private final Random random = new Random();

    public TitledData<Integer> getData(String title) {
        return TitledData.build(title, 1);
    }

    public TitledData<Integer> tryGetData(String title, boolean forceFail) throws RuntimeException {
        boolean fail = random.nextBoolean();
        if (forceFail || fail) throw new RuntimeException("It is a fail");

        return TitledData.build(title, 1);
    }

    public TitledData<Integer> recoverFromError(Throwable t) {
        return TitledData.build(t.getMessage(), 0);
    }

    public CompletableFuture<TitledData<Integer>> executeAsyncWithRandomDelay(String title, int minSeconds, int maxSeconds) {
        return executeAsyncWithRandomDelay(() -> getData(title), minSeconds, maxSeconds);
    }

    public <T> CompletableFuture<T> executeAsyncWithRandomDelay(Supplier<T> supplier, int minSeconds, int maxSeconds) {
        int delaySecs = random.nextInt(maxSeconds - minSeconds + 1) + minSeconds;

        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Waiting {} seconds", delaySecs);
                TimeUnit.SECONDS.sleep(delaySecs);

                return supplier.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                log.info("Task resolved");
            }
        });
    }
}
