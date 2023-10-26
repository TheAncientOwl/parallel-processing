package rest;

import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingDeque<String> messages = new LinkedBlockingDeque<>(10);

        Thread injector = new Thread(new MessageInjector(messages));
        Thread processor = new Thread(new MessageProcessor(messages));

        processor.start();
        Thread.sleep(5000);
        injector.start();

        injector.join();
        processor.join();
    }
}
