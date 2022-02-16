package reactor.examples.scheduler;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.examples.util.LoggerUtil;
import reactor.examples.util.TimeUtil;

public class ParallelFluxExamples {

    private final static Logger logger = LoggerFactory.getLogger(ParallelFluxExamples.class);

    @Test
    public void create(){
        Mono.fromCallable(System::currentTimeMillis)
                .repeat()
                .parallel(8) //parallelism
                .runOn(Schedulers.parallel())
                .doOnNext(time -> LoggerUtil.logInfoAndReturn(logger, "Current time {} on thread" + Thread.currentThread(), time))
                .subscribe();

        TimeUtil.sleepMillis(50);
    }
}
