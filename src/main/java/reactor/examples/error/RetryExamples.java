package reactor.examples.error;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.examples.util.LoggerUtil;
import reactor.examples.util.TimeUtil;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryExamples {

    private final static Logger logger = LoggerFactory.getLogger(RetryExamples.class);

    @Test
    public void retryOneTime() {
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) return "tick " + input;
                    throw new RuntimeException("boom");
                })
                //when input == 3, retry once
                .retry(1)
                //elapsed associates each value with the duration since previous value was emitted.
                //So tick 1 -> [Duration, tick 1]
                .elapsed()
                .subscribe(output -> LoggerUtil.logInfo(logger, output),
                        error -> LoggerUtil.logError(logger, error));

        TimeUtil.sleepMillis(2100);
    }

    @Test
    public void retryWhen() {
        AtomicInteger errorCount = new AtomicInteger();
        Flux<Object> flux = Flux.error(new IllegalArgumentException())
                .doOnError(error -> LoggerUtil.logInfoAndReturn(logger, errorCount.incrementAndGet()))
                .retryWhen(Retry.from(companion -> companion.take(3)));
        //only take could not throw error in retry
        flux.subscribe(output -> LoggerUtil.logInfo(logger, output),
                error -> LoggerUtil.logError(logger, "no error here {}", error)
        );
    }

    @Test
    public void retryWhenAndThrowError() {
        AtomicInteger errorCount = new AtomicInteger();
        Flux<String> flux =
                Flux.<String>error(new IllegalArgumentException())
                        .doOnError(e -> LoggerUtil.logInfoAndReturn(logger, errorCount.incrementAndGet()))
                        .retryWhen(Retry.from(companion ->
                                companion.map(rs -> {
                                    if (rs.totalRetries() < 3) return rs.totalRetries();
                                    else throw Exceptions.propagate(rs.failure());
                                })
                        ));

        flux.subscribe(output -> LoggerUtil.logInfo(logger, output),
                error -> LoggerUtil.logError(logger, error)
        );
    }

}
