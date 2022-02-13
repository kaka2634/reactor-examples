package reactor.examples.subscribe;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.examples.util.LoggerUtil;

public class SampleSubscriber<T> extends BaseSubscriber<T> {
    private static final Logger logger = LoggerFactory.getLogger(SampleSubscriber.class);

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        LoggerUtil.logInfo(logger, "Subscribed");
        request(1);
    }

    @Override
    protected void hookOnNext(T value) {
        LoggerUtil.logInfo(logger, value);
        request(1);
    }

    @Override
    protected void hookOnComplete() {
        LoggerUtil.logInfo(logger, "Completed");
    }
}
