package reactor.examples.create;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.util.LoggerUtil;

import java.util.Arrays;
import java.util.List;

public class SimpleCreate {
    private final static Logger logger = LoggerFactory.getLogger(SimpleCreate.class);

    @Test
    public void useJust() {
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        seq1.map(data -> LoggerUtil.logInfoAndReturn(logger, "seq1.map {}", data))
                .subscribe(data -> LoggerUtil.logInfo(logger, "seq1.subscribe {}", data));
    }

    @Test
    public void useFromIterable() {
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        seq2.map(data -> LoggerUtil.logInfoAndReturn(logger, "seq2.map {}", data))
                .subscribe(data -> LoggerUtil.logInfo(logger, "seq2.subscribe {}", data));
    }
}
