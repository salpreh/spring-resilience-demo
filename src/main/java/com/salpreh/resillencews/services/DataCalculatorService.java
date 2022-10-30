package com.salpreh.resillencews.services;

import com.salpreh.resillencews.models.TitledData;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DataCalculatorService {

    private final Random random = new Random();

    public TitledData<Integer> getData(boolean forceFail) {
        boolean fail = random.nextBoolean();
        if (forceFail || fail) throw new RuntimeException("It is a fail");

        return TitledData.build("fallback success", 1);
    }

    public TitledData<Integer> recoverFromError(Throwable t) {
        return TitledData.build(t.getMessage(), 0);
    }
}
