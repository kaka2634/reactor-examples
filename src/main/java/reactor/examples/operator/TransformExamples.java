package reactor.examples.operator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.util.LoggerUtil;

public class TransformExamples {

    private final static Logger logger = LoggerFactory.getLogger(TransformExamples.class);

    @Test
    public void map() {
        Flux.just(1, 2, 3)
                .map(i -> i + 1)
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void cast() {
        Flux.just(1, 2, 3)
                //cast type
                .cast(Integer.class)
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void index() {
        Flux.just(1, 2, 3)
                //associate with index
                .index()
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void flatMap() {
        Flux.just(1, 2, 3)
                //flatMap should return Flux<V>
                .flatMap(i -> Flux.just(i + 1))
                .subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void handle(){
        Flux.just(1, 2, 3)
                //flatMap couldn't return null data
                .flatMap(i -> {
                    if(i == 3) return null;
                    else return Flux.just(i + 1);
                })
                .subscribe(data -> LoggerUtil.logInfo(logger, data),
                        error -> LoggerUtil.logInfo(logger, error));

        Flux.just(1, 2, 3)
                //handle can check if data is null and filter it out
                .handle((i, sink) -> {
                    if(i != 3) sink.next(i + 1);
                })
                .subscribe(data -> LoggerUtil.logInfo(logger, data),
                        error -> LoggerUtil.logInfo(logger, error));
    }


}
