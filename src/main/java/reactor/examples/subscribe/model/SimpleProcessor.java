package reactor.examples.subscribe.model;

import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.examples.util.LoggerUtil;

import java.util.concurrent.ConcurrentLinkedDeque;


public class SimpleProcessor<T, R> implements Processor<T, R> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleProcessor.class);

    private volatile Subscription upstream;
    private volatile ConcurrentLinkedDeque<Subscriber<? super R>> subscribers = new ConcurrentLinkedDeque<>();


    @Override
    public void subscribe(Subscriber<? super R> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        upstream = subscription;
        upstream.request(Long.MAX_VALUE);
        LoggerUtil.logInfo(logger, "Subscribed");
    }


    @Override
    public void onNext(T t) {
        LoggerUtil.logInfo(logger, t);
        subscribers.forEach(subscriber -> {
            subscriber.onNext((R) t);
        });
    }

    @Override
    public void onError(Throwable throwable) {
        LoggerUtil.logError(logger, throwable);
    }

    @Override
    public void onComplete() {
        LoggerUtil.logInfo(logger, "Completed");
    }


}
