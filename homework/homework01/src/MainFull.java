import java.util.ArrayList;
import java.util.List;

// MORE INFO IN README.md file

class Utils {
    public static void logResult(long max, ArrayList<Integer> nums) {
        System.out.print(">> Max: " + max + " -> Nums: ");
        for (var num : nums) {
            System.out.print(num.toString() + ", ");
        }
        System.out.println("");
    }
}

class Timer {
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

class Config {
    public static final int INTERVAL_BEGIN = 1;
    public static final int INTERVAL_END = 200000;
    public static final int THREADS_COUNT = Runtime.getRuntime().availableProcessors();
}

interface ResultProvider {
    public long getMaxDivisorsCount();

    public ArrayList<Integer> getNumbers();
}

abstract class Solution implements Runnable, ResultProvider {
    protected long maxDivisorsCount = 0;
    protected ArrayList<Integer> numbers = new ArrayList<>();

    @Override
    public long getMaxDivisorsCount() {
        return this.maxDivisorsCount;
    }

    @Override
    public ArrayList<Integer> getNumbers() {
        return this.numbers;
    }
}

class SequentialSolution1 extends Solution {
    private final int INTERVAL_BEGIN;
    private final int INTERVAL_END;
    private final int STEP;

    public static long computeDivisorsCount(long x) {
        long count = 0;

        for (long i = 1; i <= x; i += 1) {
            if (x % i == 0) {
                count += 1;
            }
        }

        return count;
    }

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        for (int i = INTERVAL_BEGIN; i <= INTERVAL_END; i += STEP) {
            long divisorsCount = SequentialSolution1.computeDivisorsCount(i);

            if (divisorsCount > this.maxDivisorsCount) {
                this.maxDivisorsCount = divisorsCount;
                this.numbers.clear();
                this.numbers.add(i);
            } else if (divisorsCount == this.maxDivisorsCount) {
                this.numbers.add(i);
            }
        }
    }

    public SequentialSolution1() {
        this.INTERVAL_BEGIN = Config.INTERVAL_BEGIN;
        this.INTERVAL_END = Config.INTERVAL_END;
        this.STEP = 1;
    }

    public SequentialSolution1(int intervalBegin, int intervalEnd) {
        this.INTERVAL_BEGIN = intervalBegin;
        this.INTERVAL_END = intervalEnd;
        this.STEP = 1;
    }

    public SequentialSolution1(int intervalBegin, int intervalEnd, int step) {
        this.INTERVAL_BEGIN = intervalBegin;
        this.INTERVAL_END = intervalEnd;
        this.STEP = step;
    }

}

class SequentialSolution2 extends Solution {
    private final int INTERVAL_BEGIN;
    private final int INTERVAL_END;
    private final int STEP;

    public static long computeDivisorsCount(long x) {
        long count = 2;

        for (long i = 2, end = x / 2; i <= end; i += 1) {
            if (x % i == 0) {
                count += 1;
            }
        }

        return count;
    }

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        for (int i = INTERVAL_BEGIN; i <= INTERVAL_END; i += STEP) {
            long divisorsCount = SequentialSolution2.computeDivisorsCount(i);

            if (divisorsCount > this.maxDivisorsCount) {
                this.maxDivisorsCount = divisorsCount;
                this.numbers.clear();
                this.numbers.add(i);
            } else if (divisorsCount == this.maxDivisorsCount) {
                this.numbers.add(i);
            }
        }
    }

    public SequentialSolution2() {
        this.INTERVAL_BEGIN = Config.INTERVAL_BEGIN;
        this.INTERVAL_END = Config.INTERVAL_END;
        this.STEP = 1;
    }

    public SequentialSolution2(int intervalBegin, int intervalEnd) {
        this.INTERVAL_BEGIN = intervalBegin;
        this.INTERVAL_END = intervalEnd;
        this.STEP = 1;
    }

    public SequentialSolution2(int intervalBegin, int intervalEnd, int step) {
        this.INTERVAL_BEGIN = intervalBegin;
        this.INTERVAL_END = intervalEnd;
        this.STEP = step;
    }

}

class SequentialSolution3 extends Solution {

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        long[] divisorsCount = new long[(int) (Config.INTERVAL_END + 1)];

        for (int i = Config.INTERVAL_BEGIN; i <= Config.INTERVAL_END; i += 1) {
            for (int j = 1; j * i <= Config.INTERVAL_END; j += 1) {
                divisorsCount[i * j] += 1;
            }
        }

        for (var count : divisorsCount) {
            this.maxDivisorsCount = Math.max(this.maxDivisorsCount, count);
        }

        for (int i = Config.INTERVAL_BEGIN; i <= Config.INTERVAL_END; i += 1) {
            if (divisorsCount[i] == this.maxDivisorsCount) {
                this.numbers.add(i);
            }
        }
    }

}

class ParallelSolution1ForSequential2 extends Solution {

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        List<SequentialSolution2> solutions = new ArrayList<>();

        final int CHUNK_SIZE = Config.INTERVAL_END / Config.THREADS_COUNT;

        for (int i = 0; i < Config.THREADS_COUNT; i += 1) {
            int begin = i * CHUNK_SIZE + 1;
            int end = i == (Config.THREADS_COUNT - 1) ? Config.INTERVAL_END : (i + 1) * CHUNK_SIZE;

            SequentialSolution2 solution = new SequentialSolution2(begin, end);

            solutions.add(solution);
            threads.add(new Thread(solution));
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SequentialSolution2 solution : solutions) {
            this.maxDivisorsCount = Math.max(this.maxDivisorsCount, solution.getMaxDivisorsCount());
        }

        for (SequentialSolution2 solution : solutions) {
            if (solution.getMaxDivisorsCount() == this.maxDivisorsCount) {
                for (var num : solution.getNumbers()) {
                    this.numbers.add(num);
                }
            }
        }
    }

}

class ParallelSolution1ForSequential1 extends Solution {

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        List<SequentialSolution1> solutions = new ArrayList<>();

        final int CHUNK_SIZE = Config.INTERVAL_END / Config.THREADS_COUNT;

        for (int i = 0; i < Config.THREADS_COUNT; i += 1) {
            int begin = i * CHUNK_SIZE + 1;
            int end = i == (Config.THREADS_COUNT - 1) ? Config.INTERVAL_END : (i + 1) * CHUNK_SIZE;

            SequentialSolution1 solution = new SequentialSolution1(begin, end);

            solutions.add(solution);
            threads.add(new Thread(solution));
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SequentialSolution1 solution : solutions) {
            this.maxDivisorsCount = Math.max(this.maxDivisorsCount, solution.getMaxDivisorsCount());
        }

        for (SequentialSolution1 solution : solutions) {
            if (solution.getMaxDivisorsCount() == this.maxDivisorsCount) {
                for (var num : solution.getNumbers()) {
                    this.numbers.add(num);
                }
            }
        }
    }

}

class ParallelSolution2ForSequential1 extends Solution {

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        List<SequentialSolution1> solutions = new ArrayList<>();

        for (int i = 1; i <= Config.THREADS_COUNT; i += 1) {
            SequentialSolution1 solution = new SequentialSolution1(i, Config.INTERVAL_END, Config.THREADS_COUNT);

            solutions.add(solution);
            threads.add(new Thread(solution));
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SequentialSolution1 solution : solutions) {
            this.maxDivisorsCount = Math.max(this.maxDivisorsCount, solution.getMaxDivisorsCount());
        }

        for (SequentialSolution1 solution : solutions) {
            if (solution.getMaxDivisorsCount() == this.maxDivisorsCount) {
                for (var num : solution.getNumbers()) {
                    this.numbers.add(num);
                }
            }
        }
    }

}

class ParallelSolution2ForSequential2 extends Solution {

    @Override
    public void run() {
        this.maxDivisorsCount = 0;
        this.numbers = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        List<SequentialSolution2> solutions = new ArrayList<>();

        for (int i = 1; i <= Config.THREADS_COUNT; i += 1) {
            SequentialSolution2 solution = new SequentialSolution2(i, Config.INTERVAL_END, Config.THREADS_COUNT);

            solutions.add(solution);
            threads.add(new Thread(solution));
        }

        for (var thread : threads) {
            thread.start();
        }

        for (var thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (SequentialSolution2 solution : solutions) {
            this.maxDivisorsCount = Math.max(this.maxDivisorsCount, solution.getMaxDivisorsCount());
        }

        for (SequentialSolution2 solution : solutions) {
            if (solution.getMaxDivisorsCount() == this.maxDivisorsCount) {
                for (var num : solution.getNumbers()) {
                    this.numbers.add(num);
                }
            }
        }
    }

}

public class MainFull {
    public static void main(String[] args) {
        MainFull.benchmark("[Sequential solution 1]", new SequentialSolution1());
        MainFull.benchmark("[Sequential solution 2]", new SequentialSolution2());
        MainFull.benchmark("[Sequential solution 3]", new SequentialSolution3());
        MainFull.benchmark("[Parallel solution 1 for sequential solution 1]", new ParallelSolution1ForSequential1());
        MainFull.benchmark("[Parallel solution 1 for sequential solution 2]", new ParallelSolution1ForSequential2());
        MainFull.benchmark("[Parallel solution 2 for sequential solution 1]", new ParallelSolution2ForSequential1());
        MainFull.benchmark("[Parallel solution 2 for sequential solution 2]", new ParallelSolution2ForSequential2());
        System.out.println("------------------------------------------------");
    }

    private static void benchmark(String label, Solution provider) {
        System.out.println("------------------------------------------------");
        Timer.benchmark(label, provider, true);
        Utils.logResult(provider.getMaxDivisorsCount(), provider.getNumbers());
    }
}
