package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomRetryConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Log4j2
@Service
@RequiredArgsConstructor
public class RetryUseCase {

    private final DataCalculatorService dataCalculatorService;
    private final RetryRegistry retryRegistry;

    public TitledData<Integer> nativeRetry(boolean forceFail) {
        Supplier<TitledData<Integer>> retrySupplier = io.github.resilience4j.retry.Retry.decorateSupplier(
            retryRegistry.retry(CustomRetryConfig.NATIVE_RT),
            () -> dataCalculatorService.tryGetData("Retry success", forceFail)
        );

        return Try.ofSupplier(retrySupplier)
            .recover(dataCalculatorService::recoverFromError)
            .get();
    }

    @Retry(name = CustomRetryConfig.SPRING_RT, fallbackMethod = "errorFallback")
    public TitledData<Integer> springRetry(boolean forceFail) {
        return dataCalculatorService.tryGetData("Spring Retry success", forceFail);
    }

    private TitledData<Integer> errorFallback(boolean forceFail, Throwable t) {
        return dataCalculatorService.recoverFromError(t);
    }
}
