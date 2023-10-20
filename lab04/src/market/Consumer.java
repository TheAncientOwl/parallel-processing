package market;

import java.util.Random;

public class Consumer extends Thread {
    private Market market;

    public Consumer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            int value = random.nextInt(50) + 1;

            if (this.market.getStock() >= value) {
                System.out.println("[Consumer] >> Consumer consumed " + value + "!");

                this.market.sellStock(value);

                System.out.println("[Consumer] >> Stock: " + this.market.getStock());
            }
        }
    }
}
