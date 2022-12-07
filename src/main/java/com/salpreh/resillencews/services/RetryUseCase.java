package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomRetryConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
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
        Supplier<TitledData<Integer>> retrySupplier = Retry.decorateSupplier(
            retryRegistry.retry(CustomRetryConfig.NATIVE_RT),
            () -> dataCalculatorService.tryGetData("Retry success", forceFail)
        );

        return Try.ofSupplier(retrySupplier)
            .recover(dataCalculatorService::recoverFromError)
            .get();
    }
}
