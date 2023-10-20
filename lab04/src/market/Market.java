package market;

import java.util.concurrent.atomic.AtomicInteger;

public class Market {
    private static final int STOCK_CAP = 1000;

    private AtomicInteger stock;

    public Market(AtomicInteger stock) {
        this.stock = stock;
    }

    public void addStock(int value, Thread producer) {
        this.stock.getAndAdd(value);

        if (this.getStock() >= Market.STOCK_CAP) {
            System.out.println(">> Market suspends producer!");
            synchronized (producer) {
                try {
                    producer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sellStock(int value) {
        this.stock.getAndAdd(-value);
    }

    public int getStock() {
        return this.stock.get();
    }

    public boolean stockReachedMarketCap() {
        return this.stock.get() == Market.STOCK_CAP;
    }
}
