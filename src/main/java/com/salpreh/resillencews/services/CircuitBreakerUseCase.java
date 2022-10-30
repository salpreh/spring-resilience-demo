package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CustomCircuitBreakerConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Log4j2
public class CircuitBreakerUseCase {

    private final DataCalculatorService dataCalculatorService;
    private final CircuitBreakerRegistry cbRegistry;


    public TitledData<Integer> nativeCircuitBreaker(boolean forceFail) {

        Supplier<TitledData<Integer>> cbSupplier = cbRegistry.circuitBreaker(CustomCircuitBreakerConfig.NATIVE_CB)
            .decorateSupplier(() -> {
                log.info("Executing native CB");
                return dataCalculatorService.tryGetData(forceFail);
            });

        Try<TitledData<Integer>> recoverSupplier = Try.ofSupplier(cbSupplier)
            .recover(t -> {
                log.warn("Managed error in native CB", t);
                return dataCalculatorService.recoverFromError(t);
            });

        return recoverSupplier.get();
    }

    @CircuitBreaker(name = CustomCircuitBreakerConfig.SPRING_CB, fallbackMethod = "errorFallback")
    public TitledData<Integer> springCircuitBreaker(boolean forceFail) {
        log.info("Executing spring CB");

        return dataCalculatorService.tryGetData(forceFail);
    }

    private TitledData<Integer> errorFallback(boolean forceFail, Throwable t) {
        log.warn("Managed error in spring CB", t);
        return dataCalculatorService.recoverFromError(t);
    }
}
