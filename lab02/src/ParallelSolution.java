import java.util.ArrayList;

class ParalellSolution {
    private static final int THREADS_COUNT = 4;

    public static Result getMultiplesCount(ArrayList<Integer> nums, int base, int iterationsCount) {
        Result result = new Result(0);

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            int startIdx = i * nums.size() / THREADS_COUNT;
            int endIdx = (i + 1) * nums.size() / THREADS_COUNT;
            if (i == THREADS_COUNT - 1) {
                endIdx = nums.size();
            }

            threads.add(new ProcessingThread(result, nums, startIdx, endIdx, base, iterationsCount));
        }

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            threads.get(i).start();
        }

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static int getMultiplesCountWithoutRaceCondition(ArrayList<Integer> nums, int base,
            int iterationsCount) {
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Result> results = new ArrayList<>();

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            results.add(new Result(0));
        }

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            int startIdx = i * nums.size() / THREADS_COUNT;
            int endIdx = (i + 1) * nums.size() / THREADS_COUNT;
            if (i == THREADS_COUNT - 1) {
                endIdx = nums.size();
            }

            threads.add(new ProcessingThread(results.get(i), nums, startIdx, endIdx, base, iterationsCount));
        }

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            threads.get(i).start();
        }

        for (int i = 0; i < THREADS_COUNT; i += 1) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int result = 0;
        for (Result res : results) {
            result += res.getValue();
        }

        return result;
    }
}
