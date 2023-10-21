package sequential;

import java.util.ArrayList;

import utils.Config;
import utils.Solution;

/**
 * @brief Compute the count using improved naive divisors algorithm
 *        (passing from i:[1 to number / 2], check if number divisible by i)
 */
public class SequentialSolution2 extends Solution {
    private final int INTERVAL_BEGIN;
    private final int INTERVAL_END;

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

        for (int i = INTERVAL_BEGIN; i <= INTERVAL_END; i++) {
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
    }

    public SequentialSolution2(int intervalBegin, int intervalEnd) {
        this.INTERVAL_BEGIN = intervalBegin;
        this.INTERVAL_END = intervalEnd;
    }

}
