package market;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Market market = new Market();

        Thread producer = new Thread(new Producer(market));
        Thread consumer = new Thread(new Consumer(market));

        producer.start();
        consumer.start();

        Thread.sleep(10000);
        market.close();

        producer.join();
        consumer.join();
    }
}
