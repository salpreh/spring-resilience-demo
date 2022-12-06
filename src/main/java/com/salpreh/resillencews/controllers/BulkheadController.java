package com.salpreh.resillencews.controllers;

import com.salpreh.resillencews.models.TitledData;
import com.salpreh.resillencews.services.BulkheadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bulkhead")
@RequiredArgsConstructor
public class BulkheadController {
    private final BulkheadUseCase bulkheadUseCase;

    @GetMapping("native")
    public TitledData<Integer> getDataWithNativeBH() {
        return bulkheadUseCase.nativeBulkhead();
    }
}
