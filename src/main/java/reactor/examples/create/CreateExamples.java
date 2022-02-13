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

public class CreateExamples {
    private final static Logger logger = LoggerFactory.getLogger(CreateExamples.class);

    //Flux.create(): Asynchronous and Multi-threaded

    @Test
    public void useCreate() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        MyEventProcessor myEventProcessor = new MyEventProcessorImpl();

        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register(
                    new MyEventListener<>() {

                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                            latch.countDown();
                        }
                    });
        });

        bridge.subscribe(data -> LoggerUtil.logInfo(logger, data));

        myEventProcessor.dataChunk("foo", "bar", "baz");
        myEventProcessor.processComplete();

        latch.await();
    }

}
