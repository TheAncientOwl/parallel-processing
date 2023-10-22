package sequential;

import java.util.ArrayList;

import utils.Config;
import utils.Solution;

/**
 * @brief Compute divisorsCount array -> find max
 *        -> find numbers with max divisors count.
 */
public class SequentialSolution3 extends Solution {

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
