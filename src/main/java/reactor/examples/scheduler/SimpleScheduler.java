package reactor.examples.scheduler;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.examples.util.LoggerUtil;

import java.util.concurrent.CountDownLatch;

public class SimpleScheduler {
    Logger logger = LoggerFactory.getLogger(SimpleScheduler.class);

    @Test
    public void newThread() throws InterruptedException {
        final Mono<String> mono = Mono.just("hello ");

        Thread t = new Thread(() -> mono
                .map(msg -> msg + "thread ")
                .subscribe(v ->
                        logger.info(v)
                )
        );
        t.start();
        t.join();
    }


    @Test
    public void useSubscribeOn() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux.just(1)
                //2. Run on parallel-scheduler-1
                .map(i -> LoggerUtil.logInfoAndReturn(logger, "inside first map and value {} ", i))
                .subscribeOn(s)
                //3. Run on parallel-scheduler-1
                .map(i -> LoggerUtil.logInfoAndReturn(logger, "inside second map and value {} ", i.toString()));


        Thread t = new Thread(() -> {
            //1. Run on Thread-0
            logger.info("flux subscribe line");
            flux.subscribe(value -> {
                //4. Run on parallel-scheduler-1
                logger.info("inside subscribe and value {}", value);
                latch.countDown();
            });
        });
        t.start();
        latch.await();
    }

    @Test
    public void usePublishOn() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux.just(1)
                //2. Run on Thread-0
                .map(i -> LoggerUtil.logInfoAndReturn(logger, "inside first map and value {} ", i))
                .subscribeOn(s)
                //3. Run on parallel-scheduler-1
                .map(i -> LoggerUtil.logInfoAndReturn(logger, "inside second map and value {} ", i.toString()));


        Thread t = new Thread(() -> {
            //1. Run on Thread-0
            logger.info("flux subscribe line");
            flux.subscribe(value -> {
                //4. Run on parallel-scheduler-1
                logger.info("inside subscribe and value {}", value);
                latch.countDown();
            });
        });
        t.start();
        latch.await();
    }
}
