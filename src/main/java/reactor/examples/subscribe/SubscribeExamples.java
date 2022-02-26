package reactor.examples.subscribe;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.examples.subscribe.model.SimpleBaseSubscriber;
import reactor.examples.util.LoggerUtil;

public class SubscribeExamples {

    private final static Logger logger = LoggerFactory.getLogger(SubscribeExamples.class);

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
    public void useSubscriptionMethod() {
        Flux<Integer> ints = Flux.range(1, 4);

        ints.subscribe(i -> LoggerUtil.logInfo(logger, i),
                error -> LoggerUtil.logInfo(logger, "Encounter error {}", error),
                () -> LoggerUtil.logInfo(logger, "Done"),
                sub -> sub.request(4));

    }




    @Test
    public void useBaseSubscriber() {
        SimpleBaseSubscriber<Integer> subscriber = new SimpleBaseSubscriber<>();
        Flux<Integer> ints = Flux.range(1, 4);

        ints.subscribe(subscriber);
    }


    /* Make sure subscribe correct chain*/

    @Test
    public void subscribeNotCorrect(){
        Flux<String> flux= Flux.just("something", "chain");
        flux.map(secret -> secret.replaceAll(".", "*"));
        //Only subscribe the just! so it is wrong
        flux.subscribe(next -> LoggerUtil.logInfo(logger, "Received: " + next));
    }

    @Test
    public void subscribeCorrect(){
        Flux<String> flux= Flux.just("something", "chain");
        //set result to flux
        flux = flux.map(secret -> secret.replaceAll(".", "*"));
        flux.subscribe(next -> LoggerUtil.logInfo(logger, "Received: " + next));
    }

    @Test
    public void subscribeBetter(){
        Disposable secrets = Flux
                .just("something", "chain")
                .map(secret -> secret.replaceAll(".", "*"))
                .subscribe(next -> LoggerUtil.logInfo(logger, "Received: " + next));
    }



}
