package com.salpreh.resillencews.services;

import com.salpreh.resillencews.models.TitledData;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class FallbackUseCase {

    public final Random random = new Random();

    public TitledData<Integer> nativeFallback(boolean forceFail) {
        return Try.ofSupplier(() -> {
            log.info("Executing native fallback");

            return getData(forceFail);
        }).recover(t -> {
            log.warn("Error in native fallback", t);
            return TitledData.build(t.getMessage(), 0);
        }).get();
    }

    @Retry(name = "directFallback", fallbackMethod = "errorFallback")
    public TitledData<Integer> springFallback(boolean forceFail) {
        log.info("Executing spring fallback");

        return getData(forceFail);
    }

    private TitledData<Integer> errorFallback(boolean forceFail, RuntimeException t) {
        log.warn("Error in spring fallback", t);
        return TitledData.build(t.getMessage(), 0);
    }

    private TitledData<Integer> getData(boolean forceFail) {
        boolean fail = random.nextBoolean();
        if (forceFail || fail) throw new RuntimeException("It is a fail");

        return TitledData.build("fallback success", 1);
    }
}
