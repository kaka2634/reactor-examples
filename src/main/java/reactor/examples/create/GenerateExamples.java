package reactor.examples.create;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.util.LoggerUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GenerateExamples {
    private final static Logger logger = LoggerFactory.getLogger(GenerateExamples.class);

    //Flux.generate()  Synchronous create sequence

    @Test
    public void useGenerate() {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                }
        );

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void useGenerateWithMutableVariant() {
        Flux<String> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + state + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }
        );

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void useGenerateWithCleanUpConsumer() {
        Flux<String> flux = Flux.generate(
                AtomicInteger::new,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state.get());
                    if (state.get() == 10) sink.complete();
                    state.getAndIncrement();
                    return state;
                },
                //Clean up consumer after complete
                (state) -> LoggerUtil.logInfo(logger, "Clean up final state: {}", state)
        );

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

}
