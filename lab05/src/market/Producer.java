package market;

import java.util.Random;

public class Producer implements Runnable {

    private Market market;

    public Producer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {
        while (!this.market.isClosed()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Random random = new Random();
            int value = random.nextInt(100) + 1;

            System.out.println("[Producer] >> Producer produced " + value + "!");

            try {
                this.market.addStock(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[INFO] Producer closed!");
    }
}
