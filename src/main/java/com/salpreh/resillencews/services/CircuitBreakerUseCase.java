package com.salpreh.resillencews.services;

import com.salpreh.resillencews.config.CircuitBreakerCustomConfig;
import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
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
        Supplier<TitledData<Integer>> cbSupplier = cbRegistry.circuitBreaker(CircuitBreakerCustomConfig.NATIVE_CB)
            .decorateSupplier(() -> dataCalculatorService.getData(forceFail));

        Try<TitledData<Integer>> retrySupplier = Try.ofSupplier(cbSupplier)
            .recover(t -> {
                log.warn("Error in native CB", t);
                return dataCalculatorService.recoverFromError(t);
            });

        return retrySupplier.get();
    }
}
