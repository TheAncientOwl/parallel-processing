public class PrimeThread extends Thread {
    private long start;
    private long end;
    private int count;
    private int step;

    public PrimeThread(long start, long end) {
        this.start = start;
        this.end = end;
        this.count = 0;
        this.step = 1;
    }

    public PrimeThread(long start, long end, int step) {
        this.start = start;
        this.end = end;
        this.count = 0;
        this.step = step;
    }

    public void run() {
        double testStart = System.currentTimeMillis();
        this.count = Prime.count(start, end, step);
        double testEnd = System.currentTimeMillis();

        System.out.println(
                String.format("Thread >> duration = %f seconds", ((testEnd - testStart) / 1000)));
    }

    public int getCount() {
        return this.count;
    }
}
