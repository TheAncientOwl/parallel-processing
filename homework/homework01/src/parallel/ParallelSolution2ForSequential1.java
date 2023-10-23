package parallel;

import java.util.ArrayList;
import java.util.List;

import sequential.SequentialSolution1;
import utils.Solution;
import utils.Config;

/**
 * @brief Spread the numbers in the interval evenly accross all threads.
 *        Use SequentialSolution1 divisors approach
 */
public class ParallelSolution2ForSequential1 extends Solution {

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
