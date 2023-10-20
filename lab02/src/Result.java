public class Result {
    private int value;

    public Result(int value) {
        this.value = value;
    }

    public synchronized void increment() {
        this.value += 1;
    }

    public void add(int value) {
        this.value += value;
    }

    public int getValue() {
        return this.value;
    }
}
