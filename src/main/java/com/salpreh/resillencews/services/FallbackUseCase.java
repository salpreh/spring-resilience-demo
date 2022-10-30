package com.salpreh.resillencews.services;

import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class FallbackUseCase {

    private final DataCalculatorService dataCalculatorService;

    public TitledData<Integer> nativeFallback(boolean forceFail) {
        return Try.ofSupplier(() -> {
            log.info("Executing native fallback");

            return dataCalculatorService.tryGetData(forceFail);
        }).recover(t -> {
            log.warn("Managed error in native fallback", t);
            return dataCalculatorService.recoverFromError(t);
        }).get();
    }

    @Retry(name = "directFallback", fallbackMethod = "errorFallback")
    public TitledData<Integer> springFallback(boolean forceFail) {
        log.info("Executing spring fallback");

        return dataCalculatorService.tryGetData(forceFail);
    }

    private TitledData<Integer> errorFallback(boolean forceFail, Throwable t) {
        log.warn("Managed error in spring fallback", t);
        return dataCalculatorService.recoverFromError(t);
    }
}
