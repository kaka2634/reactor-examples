package reactor.examples.operator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.examples.util.LoggerUtil;
import reactor.examples.util.TimeUtil;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransformExamples {

    private final static Logger logger = LoggerFactory.getLogger(TransformExamples.class);


    /**
     *  1-to-1 basis (eg. strings to their length)
     */
    @Test
    public void map() {
        Flux.just(1, 2, 3)
                .map(i -> i + 1)
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    /**
     * just casting it
     */
    @Test
    public void cast() {
        Flux.just(1, 2, 3)
                //cast type
                .cast(Integer.class)
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    /**
     * materialize each source value’s index
     */
    @Test
    public void index() {
        Flux.just(1, 2, 3)
                //associate with index
                .index()
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    /**
     * 1-to-n basis (eg. strings to their characters)
     */
    @Test
    public void flatMap() {
        Flux.just(1, 2, 3)
                //flatMap should return Flux<V>
                .flatMap(i -> Flux.just(i + 1))
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }


    /**
     * 1-to-n basis with programmatic behavior for each source element and/or state
     */
    @Test
    public void handle() {
        Flux.just(1, 2, 3)
                //flatMap couldn't return null data
                .flatMap(i -> {
                    if (i == 3) return null;
                    else return Flux.just(i + 1);
                })
                .subscribe(data -> LoggerUtil.logInfo(logger, data),
                        error -> LoggerUtil.logInfo(logger, error));

        Flux.just(1, 2, 3)
                //handle can check if data is null and filter it out
                .handle((i, sink) -> {
                    if (i != 3) sink.next(i + 1);
                })
                .subscribe(data -> LoggerUtil.logInfo(logger, data),
                        error -> LoggerUtil.logInfo(logger, error));
    }


    /**
     * running an asynchronous task for each source item
     * (eg. urls to http request): flatMap (Flux|Mono) + an async Publisher-returning method
     */
    @Test
    public void useMonoEmpty() {
        Mono<Void> mono = Mono.just(1)
                .flatMap(data -> {
                    LoggerUtil.logInfo(logger, data);
                    //empty() could use to represent for null/void in reactor
                    return Mono.empty();
                });
        mono.subscribe();

        Mono<Object> empty = Mono.empty()
                .switchIfEmpty(Mono.just(2));

        empty.subscribe(data -> LoggerUtil.logInfo(logger, data));

    }

    /**
     * pre-set elements to an existing sequence
     * at the start
     */
    @Test
    public void startWith() {
        Flux<Integer> flux = Flux.range(3, 2);
        flux = flux.startWith(Arrays.asList(1, 2));
        flux.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    /**
     * pre-set elements to an existing sequence
     * at the end
     */
    @Test
    public void concatWithValues() {
        Flux<Integer> flux = Flux.range(3, 2);
        flux = flux.concatWithValues(5, 6);
        flux.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }


    @Test
    public void collect() {
        Flux<Integer> flux = Flux.range(1, 4);
        Mono<List<Integer>> mono = flux.collect(Collectors.toList());

        mono.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void collectList() {
        Flux<Integer> flux = Flux.range(1, 4);
        Mono<List<Integer>> mono = flux.collectList();

        mono.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }


    @Test
    public void repeat() {
        //Repeat can use to tranform a Mono to Flux
        Mono<Integer> mono = Mono.just(1);
        Flux<Integer> flux = mono.repeat();
    }


    @Test
    public void repeatWithNumber() {
        Flux<Integer> flux = Flux.range(1, 4).repeat(3);

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data),
                error -> LoggerUtil.logError(logger, error),
                () -> LoggerUtil.logInfo(logger, "COMPLETED"));
    }


    @Test
    public void switchOnNext() {
        //flux1 complete later than
        Flux<Long> flux1 = Flux.interval(Duration.ofMillis(200), Schedulers.single())
                .take(2);

        Flux.switchOnNext(flux1.map(interval -> {
            LoggerUtil.logInfo(logger, interval);
            //For every event in flux, new one Mono.delay as publisher
            return Mono.delay(Duration.ofMillis(100));
        }))
                .subscribe(index -> LoggerUtil.logInfo(logger, "flux1 delay onNext called: " + index),
                        error -> LoggerUtil.logError(logger, "flux1 error called", error),
                        () -> LoggerUtil.logInfo(logger, "flux1 delay completed"));

        TimeUtil.sleepSeconds(1);

        LoggerUtil.logInfo(logger, "========= NEXT log =========");
        //flux2 complete before delay event
        Flux<Long> flux2 = Flux.interval(Duration.ofMillis(200), Schedulers.single())
                .take(2);

        Flux.switchOnNext(flux2.map(interval -> {
            LoggerUtil.logInfo(logger, interval);
            //delay only happened when no more events in flux2
            return Mono.delay(Duration.ofMillis(300));
        }))
                .subscribe(index -> LoggerUtil.logInfo(logger, "flux2 delay onNext called"),
                        error -> LoggerUtil.logError(logger, "flux2 delay error called", error),
                        () -> LoggerUtil.logInfo(logger, "flux2 delay completed"));


        TimeUtil.sleepSeconds(2);

    }


}
