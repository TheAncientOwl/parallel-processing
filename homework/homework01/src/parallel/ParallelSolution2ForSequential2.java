package parallel;

import java.util.ArrayList;
import java.util.List;

import sequential.SequentialSolution2;
import utils.Config;
import utils.Solution;

/**
 * @brief Spread the numbers in the interval evenly accross all threads.
 *        Use
 */
public class ParallelSolution2ForSequential2 extends Solution {

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
