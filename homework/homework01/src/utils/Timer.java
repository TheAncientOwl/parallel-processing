package utils;

public class Timer {
    private long start;
    private String label;

    public Timer(String label, boolean log) {
        this.label = label;
        this.start(log);
    }

    public void start(boolean log) {
        if (log) {
            System.out.println(">> Timer start: " + this.label);
        }
        this.start = System.currentTimeMillis();
    }

    public long elapsedMillis() {
        long now = System.currentTimeMillis();
        return now - start;
    }

    public long elapsedSeconds() {
        return this.elapsedMillis() / 1000;
    }

    private void printElapsed(String label, long value, String unit) {
        System.out.println(">> Timer ended: " + label + " " + String.format("%3d", value) + unit);
    }

    public void printElapsedMillis(String label) {
        this.printElapsed(label, this.elapsedMillis(), "ms");
    }

    public void printElapsedSeconds(String label) {
        this.printElapsed(label, this.elapsedSeconds(), "s");
    }

    public void printElapsedVerbose(String label) {
        this.printElapsed(label, this.elapsedSeconds(), "s" + " (" + this.elapsedMillis() + "ms)");
    }

    public static void benchmark(String label, Runnable runnable, boolean seconds) {
        Timer timer = new Timer(label, true);

        runnable.run();

        timer.printElapsedVerbose(label);
    }
}
