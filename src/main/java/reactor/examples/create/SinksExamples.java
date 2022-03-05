package reactor.examples.create;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinksExamples {
    private final static Logger logger = LoggerFactory.getLogger(SinksExamples.class);

/*
many().multicast(): a sink that will transmit only newly pushed data to its subscribers, honoring their backpressure
                    (newly pushed as in "after the subscriber’s subscription").

many().unicast(): same as above, with the twist that data pushed before the first subscriber registers is buffered.

many().replay(): a sink that will replay a specified history size of pushed data to new subscribers then continue
                 pushing new data live.

one(): a sink that will play a single element to its subscribers

empty(): a sink that will play a terminal signal only to its subscribers (error or complete),
         but can still be viewed as a Mono<T> (notice the generic type <T>).

 */

    /**
     *
     */
    @Test
    public void useSinksManyReplay() {
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);

        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        replaySink.tryEmitNext(3);

        //Complete for take while
        replaySink.tryEmitNext(11);

        Flux<Integer> fluxView = replaySink.asFlux();

        fluxView.takeWhile(i -> i < 10)
                .log()
                //block until complete signal
                .blockLast();

    }

}
