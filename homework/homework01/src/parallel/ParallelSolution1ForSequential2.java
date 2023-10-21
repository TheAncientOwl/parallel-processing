package parallel;

import java.util.ArrayList;
import java.util.List;

import sequential.SequentialSolution2;
import utils.Config;
import utils.Solution;

/**
 * @brief Divide the range into chunks and compute for each chunk
 *        (on separate threads) using SequentialSolution2 algorithm
 */
public class ParallelSolution1ForSequential2 implements Solution {
    private long maxDivisorsCount = 0;
    private ArrayList<Integer> numbers = new ArrayList<>();

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

    @Override
    public long getMaxDivisorsCount() {
        return this.maxDivisorsCount;
    }

    @Override
    public ArrayList<Integer> getNumbers() {
        return this.numbers;
    }

}
