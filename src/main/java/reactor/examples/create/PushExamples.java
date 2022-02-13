package reactor.examples.create;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.create.model.MyEventListener;
import reactor.examples.create.model.MyEventProcessor;
import reactor.examples.create.model.MyEventProcessorImpl;
import reactor.examples.util.LoggerUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PushExamples {
    //Flux.push() Asynchronous but single-threaded

    private final static Logger logger = LoggerFactory.getLogger(PushExamples.class);

    @Test
    public void usePush() throws InterruptedException {
        MyEventProcessor myEventProcessor = new MyEventProcessorImpl();

        //Use multi-thread pool may lost some values
        //MyEventProcessor myEventProcessor = new MyEventProcessorImpl(3);

        Flux<String> bridge = Flux.push(sink -> {
            myEventProcessor.register(
                    new MyEventListener<>() {
                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });
        });

        bridge.subscribe(data -> LoggerUtil.logInfo(logger, data));

        myEventProcessor.dataChunk("foo", "bar", "bazz");
        myEventProcessor.dataChunk("foo2", "bar2", "bazz2");
        myEventProcessor.dataChunk("foo3", "bar3", "bazz3");

        TimeUnit.SECONDS.sleep(2);
        myEventProcessor.processComplete();

    }

}
