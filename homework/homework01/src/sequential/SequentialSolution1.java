package sequential;

import java.util.ArrayList;

import utils.Config;
import utils.Solution;

/**
 * @brief Compute the count using naive divisors algorithm
 *        (passing from i:[1 to number], check if number divisible by i).
 */
public class SequentialSolution1 extends Solution {
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
