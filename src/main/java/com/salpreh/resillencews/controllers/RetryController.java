package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.RetryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("retry")
@RequiredArgsConstructor
public class RetryController {
    private final RetryUseCase retryUseCase;

    @GetMapping("native")
    public TitledData<Integer> getWithNativeRetry(@RequestParam(defaultValue = "false") boolean forceFail) {
        return retryUseCase.nativeRetry(forceFail);
    }
}
