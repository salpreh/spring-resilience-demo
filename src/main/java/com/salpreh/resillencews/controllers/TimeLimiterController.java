package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.TimeLimiterUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@Log4j2
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
        try {
            return timeLimiterUseCase.springTimeLimiter().get();
        } catch (Throwable t) {
            log.error("Error while processing timelimiter request", t);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process request", t);
        }
    }
}
