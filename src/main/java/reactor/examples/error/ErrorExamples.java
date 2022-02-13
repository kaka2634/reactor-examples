package reactor.examples.error;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.util.LoggerUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ErrorExamples {
    private final static Logger logger = LoggerFactory.getLogger(ErrorExamples.class);

    /* onErrorReturn: Static Value */

    @Test
    public void onErrorReturn(){
        Flux<String> flux = Flux.just(3, 1, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                //Return fallbackValue for all error occur
                .onErrorReturn("Divided by zero :(");

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data));
    }

    @Test
    public void onErrorReturnWithPredicate(){
        Flux<String> flux = Flux.just(3, 1, 0)
                .map(i -> {
                    if(i == 1) throw new RuntimeException("meet1");
                    return "100 / " + i + " = " + (100 / i);
                })
                //Only error message is meet1 then return fallbackValue
                //Attention: other error will be swallow if not throw out again
                .onErrorReturn(e -> e.getMessage().equals("meet1")
                        ,"Meet 1");

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data),
                error -> LoggerUtil.logError(logger, error));
    }

    /* onErrorResume: fallback method*/
    @Test
    public void onErrorResume(){
        Flux<String> flux = Flux.just("key1", "timeout1", "unknown", "key2")
                .flatMap(k -> callExternalService(k)
                        .onErrorResume(error -> {
                            if (error instanceof TimeoutException)
                                return getFromCache(k);
                            else if (error instanceof UnknownError)
                                return Flux.just("Ignore unknown error");
                            else
                                return Flux.error(error);
                        })
                );

        flux.subscribe(data -> LoggerUtil.logInfo(logger, data),
                //Error will stop next
                error -> LoggerUtil.logError(logger, error));

    }

    private Flux<String> callExternalService(String key) {
        if (key.equals("key2"))
            return Flux.error(new IllegalArgumentException("key2 is illegal"));
        if (key.startsWith("timeout1"))
            return Flux.error(new TimeoutException());
        if (key.startsWith("unknown"))
            return Flux.error(new UnknownError());
        return Flux.just(key.replace("key", "value"));
    }

    private final static Map<String, String> cache = new HashMap<>(){{
        put("timeout1", "cache value1");
        put("timeout2", "cache value2");
    }};

    private Flux<String> getFromCache(String key){
        return Flux.just(cache.get(key));
    }
}
