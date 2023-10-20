import java.util.ArrayList;

public class ProcessingThread extends Thread {
    private Result result;
    private ArrayList<Integer> nums;
    private int startIdx;
    private int endIdx;
    private int base;
    private int iterationsCount;

    public ProcessingThread(Result result, ArrayList<Integer> nums, int startIdx, int endIdx, int base,
            int iterationsCount) {
        this.result = result;
        this.nums = nums;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.base = base;
        this.iterationsCount = iterationsCount;
    }

    @Override
    public void run() {
        for (int j = 0; j < iterationsCount; j += 1) {
            for (int i = startIdx; i < endIdx; i += 1) {
                if (nums.get(i) % base == 0) {
                    result.increment();
                }
            }
        }
    }
}
