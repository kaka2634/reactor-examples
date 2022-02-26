package reactor.examples.subscribe.model;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.examples.util.LoggerUtil;

public class SimpleSubscriber<T> implements Subscriber<T> {

    private final static Logger logger = LoggerFactory.getLogger(SimpleSubscriber.class);

    @Override
    public void onSubscribe(Subscription subscription) {
        //Subscriber.onSubscribe should tell request number
        subscription.request(Long.MAX_VALUE);
        LoggerUtil.logInfo(logger, "SampleSubscriber subscribed");
    }

    @Override
    public void onNext(T value) {
        LoggerUtil.logInfo(logger, value);
    }

    @Override
    public void onError(Throwable throwable) {
        LoggerUtil.logError(logger, throwable);
    }

    @Override
    public void onComplete() {
        LoggerUtil.logInfo(logger, "SampleSubscriber Completed");
    }
}
