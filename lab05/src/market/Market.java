package market;

public class Market {

    private static final int STOCK_CAP = 500;

    private int stock = 0;
    private volatile boolean isClosed = false;

    public void close() {
        System.out.println("[********************* [Closing Market] *********************]");
        this.isClosed = true;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public synchronized void addStock(int stockCount) throws InterruptedException {
        // notify consumer to resume
        this.notifyAll();

        System.out.println(">> (+) Adding " + stockCount + " stock");
        this.stock += stockCount;
        System.out.println(">> Current stock: " + this.stock);

        if (this.stock >= Market.STOCK_CAP) {
            System.out.println("[**********INFO**********] >> Producer stopped!");
            this.wait();
        }
    }

    public synchronized void removeStock(int stockCount) throws InterruptedException {
        // notify producer to resume
        this.notifyAll();

        if (this.stock < 0) {
            System.out.println("[**********INFO**********] >> Consumer stopped!");
            this.wait();
        } else {
            System.out.println(">> (-) Removing " + stockCount + " stock");
            this.stock -= stockCount;
            System.out.println(">> Current stock: " + this.stock);
        }
    }

    public int getStock() {
        return this.stock;
    }
}
