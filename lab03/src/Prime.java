import java.util.ArrayList;
import java.util.List;

public class Prime {
    public static boolean is(long x) {
        for (long i = 2, end = x / 2; i <= end; i += 1) {
            if (x % 2 == 0) {
                return false;
            }
        }

        return true;
    }

    public static int count(long start, long end) {
        return count(start, end, 1);
    }

    public static int count(long start, long end, long step) {
        int count = 0;

        for (long i = start; i < end; i += step) {
            if (is(i)) {
                count += 1;
            }
        }

        return count;
    }

    public static int countParallel(long start, long end, int threadsCount) {
        return countParallel(start, end, threadsCount, 1);
    }

    public static int countParallel(long start, long end, int threadsCount, int step) {
        if (start == 1) {
            start = 0;
        }

        List<PrimeThread> threads = new ArrayList<>();
        for (int i = 0; i < threadsCount; i += 1) {
            threads.add(new PrimeThread(start + (i * 2 + 1), end, 2 * threadsCount));
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

        int result = 0;
        for (var thread : threads) {
            result += thread.getCount();
        }

        return result;
    }
}
