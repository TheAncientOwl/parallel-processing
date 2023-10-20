package market;

import java.util.Random;

public class Producer extends Thread {
    private Market market;

    public Producer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            int value = random.nextInt(100) + 1;

            System.out.println("[Producer] >> Producer produced " + value + "!");

            this.market.addStock(value, this);

            System.out.println("[Producer] >> Stock: " + this.market.getStock());
        }
    }
}
