package reactor.examples.create.model;

public interface MyEventProcessor {

    void register(MyEventListener<String> eventListener);

    void dataChunk(String... values);

    void processComplete();

}
