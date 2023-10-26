package market;

import java.util.Random;

public class Consumer implements Runnable {

    private Market market;

    public Consumer(Market market) {
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

            System.out.println("[Consumer] >> Consumer consumed " + value + "!");

            try {
                this.market.removeStock(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[INFO] Consumer closed!");
    }
}
