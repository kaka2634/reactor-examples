package reactor.examples.operator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.examples.util.LoggerUtil;
import reactor.examples.util.TimeUtil;

import java.time.Duration;

public class TimeExamples {
    private final static Logger logger = LoggerFactory.getLogger(TimeExamples.class);

    @Test
    public void useMonoDelay() {
        //Create a Mono which delays an onNext signal by a given duration on a default Scheduler(parallel-1) and completes.
        Mono.delay(Duration.ofSeconds(1))
                .subscribe(index -> LoggerUtil.logInfo(logger, "delay onNext called"),
                        error -> LoggerUtil.logError(logger, "delay error called", error),
                        () -> LoggerUtil.logInfo(logger, "delay completed"));

        //Sleep main to wait delay thread complete
        TimeUtil.sleepSeconds(2);
    }
}
