package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.TimeLimiterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("timelimiter")
public class TimeLimiterController {

    private final TimeLimiterUseCase timeLimiterUseCase;

    @GetMapping("native")
    public TitledData<Integer> getDataWithNativeTL() {
        return timeLimiterUseCase.nativeTimeLimiter();
    }

    @GetMapping("spring")
    public TitledData<Integer> getDataWithSpringTL() {
        return null; // TODO: Implement
    }
}
