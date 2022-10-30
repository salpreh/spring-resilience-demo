package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.RateLimiterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("ratelimiter")
public class RateLimiterController {

    private final RateLimiterUseCase rateLimiterUseCase;

    @GetMapping("native")
    public TitledData<Integer> getDataWithNativeRL() {
        return rateLimiterUseCase.nativeRateLimiter();
    }
}
