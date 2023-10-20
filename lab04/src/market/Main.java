package market;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Market market = new Market(new AtomicInteger(0));

        Producer producer = new Producer(market);

        Consumer consumer1 = new Consumer(market);
        Consumer consumer2 = new Consumer(market);

        producer.start();
        consumer1.start();
        consumer2.start();

        producer.join();
        consumer1.join();
    }
}
