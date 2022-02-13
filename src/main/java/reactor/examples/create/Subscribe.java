package reactor.examples.create;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.util.LoggerUtil;

public class Subscribe {

    private final static Logger logger = LoggerFactory.getLogger(Subscribe.class);

    @Test
    public void useNoArgumentMethod() {
        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe();
    }

    @Test
    public void useConsumerMethod() {
        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe(i -> LoggerUtil.logInfo(logger, i));
    }

    @Test
    public void useErrorMethod() {
        Flux<Integer> ints = Flux.range(1, 4)
                .map(i -> {
                    if (i <= 3) return i;
                    throw new RuntimeException("Go to 4");
                });
        ints.subscribe(i -> LoggerUtil.logInfo(logger, i),
                error -> LoggerUtil.logInfo(logger, "Encounter error {}", error));
    }

    @Test
    public void useCompleteMethod() {
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> LoggerUtil.logInfo(logger, i),
                error -> LoggerUtil.logInfo(logger, "Encounter error {}", error),
                () -> LoggerUtil.logInfo(logger, "Done"));
    }

    @Test
    public void useSubscriptionMethod(){
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> LoggerUtil.logInfo(logger, i),
                error -> LoggerUtil.logInfo(logger, "Encounter error {}", error),
                () -> LoggerUtil.logInfo(logger, "Done"),
                sub -> sub.request(2));

    }

}
