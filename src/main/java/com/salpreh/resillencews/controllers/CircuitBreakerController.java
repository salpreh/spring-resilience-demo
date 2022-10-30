package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.CircuitBreakerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("circuitbreaker")
public class CircuitBreakerController {

    private final CircuitBreakerUseCase circuitBreakerUseCase;

    @GetMapping("native")
    public TitledData<Integer> getDataWithNativeCB(@RequestParam(defaultValue = "false") boolean forceFail) {
        return circuitBreakerUseCase.nativeCircuitBreaker(forceFail);
    }
}
