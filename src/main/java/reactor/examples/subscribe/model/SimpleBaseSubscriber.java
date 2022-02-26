package reactor.examples.subscribe.model;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.examples.util.LoggerUtil;

public class SimpleBaseSubscriber<T> extends BaseSubscriber<T> {
    private static final Logger logger = LoggerFactory.getLogger(SimpleBaseSubscriber.class);


    /**
     * Hook to add additional task for {@code onSubscribe()}
     * {@code this.subscription = subscription} is done by {@code BaseSubscribe.onSubscribe()}
     *
     */
    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        //Use default subscription.request(9223372036854775807L);
        LoggerUtil.logInfo(logger, "SampleBaseSubscriber Subscribed");
    }


    @Override
    protected void hookOnNext(T value) {
        LoggerUtil.logInfo(logger, value);
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        LoggerUtil.logError(logger, throwable);
    }

    @Override
    protected void hookOnComplete() {
        LoggerUtil.logInfo(logger, "SampleBaseSubscriber Completed");
    }
}
