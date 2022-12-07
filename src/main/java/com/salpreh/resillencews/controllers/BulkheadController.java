package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.BulkheadUseCase;
import com.salpreh.resillencews.services.DataCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bulkhead")
@RequiredArgsConstructor
public class BulkheadController {
    private final BulkheadUseCase bulkheadUseCase;
    private final DataCalculatorService dataCalculatorService;

    @GetMapping("native")
    public TitledData<Integer> getDataWithNativeBH() {
        return bulkheadUseCase.nativeBulkhead();
    }

    @GetMapping("spring")
    public TitledData<Integer> getDataWithSpringBH() {
        bulkheadUseCase.springBulkhead();

        return dataCalculatorService.getData("Spring bulkhead");
    }
}
