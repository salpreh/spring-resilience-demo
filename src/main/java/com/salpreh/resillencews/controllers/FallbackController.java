package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.FallbackUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fallback")
@RequiredArgsConstructor
public class FallbackController {

    private final FallbackUseCase fallbackUseCase;

    @GetMapping("native")
    public TitledData<Integer> getDataWithNativeFallback(@RequestParam(defaultValue = "false") boolean forceFail) {
        return fallbackUseCase.nativeFallback(forceFail);
    }

    @GetMapping("spring")
    public TitledData<Integer> getDataWithSpringFallback(@RequestParam(defaultValue = "false") boolean forceFail) {
        return fallbackUseCase.springFallback(forceFail);
    }
}
