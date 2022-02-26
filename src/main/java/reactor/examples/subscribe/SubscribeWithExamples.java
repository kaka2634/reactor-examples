package reactor.examples.subscribe;

import org.junit.Test;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.subscribe.model.SimpleBaseSubscriber;
import reactor.examples.subscribe.model.SimpleProcessor;
import reactor.examples.subscribe.model.SimpleSubscriber;

public class SubscribeWithExamples {

    private final static Logger logger = LoggerFactory.getLogger(SubscribeWithExamples.class);

    /**
     * SubscribeWith cn replace deprecated subscribe with SubscriptionMethod,
     * Because users tend to forget to request the subscription
     * SubscribeWith param is subscribe and return the subscribe
     */
    @Test
    public void subscribeWithBaseSubscriber() {
        Flux<Integer> ints = Flux.range(1, 4);

        Subscriber<Integer> subscriber = ints.subscribeWith(new SimpleBaseSubscriber<>());

        //subscriber has completed at 4
        Flux<Integer> ints2 = Flux.just(5, 6);
        ints2.subscribe(subscriber);
    }

    @Test
    public void subscribeWithProcessor() {
        Flux<Integer> ints = Flux.range(1, 4);

        Processor<Integer, String> processor = new SimpleProcessor<>();
        processor.subscribe(new SimpleSubscriber<>());

        ints.subscribeWith(processor);
    }


}
