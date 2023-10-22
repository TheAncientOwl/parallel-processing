import parallel.ParallelSolution1ForSequential1;
import parallel.ParallelSolution1ForSequential2;
import parallel.ParallelSolution2ForSequential1;
import parallel.ParallelSolution2ForSequential2;
import sequential.SequentialSolution1;
import sequential.SequentialSolution2;
import sequential.SequentialSolution3;
import utils.Solution;
import utils.Timer;
import utils.Utils;

public class Main {
    public static void main(String[] args) {
        Main.benchmark("[Sequential solution 1]", new SequentialSolution1());
        Main.benchmark("[Sequential solution 2]", new SequentialSolution2());
        Main.benchmark("[Sequential solution 3]", new SequentialSolution3());
        Main.benchmark("[Parallel solution 1 for sequential solution 1]", new ParallelSolution1ForSequential1());
        Main.benchmark("[Parallel solution 1 for sequential solution 2]", new ParallelSolution1ForSequential2());
        Main.benchmark("[Parallel solution 2 for sequential solution 1]", new ParallelSolution2ForSequential1());
        Main.benchmark("[Parallel solution 2 for sequential solution 2]", new ParallelSolution2ForSequential2());
        System.out.println("------------------------------------------------");
    }

    private static void benchmark(String label, Solution provider) {
        System.out.println("------------------------------------------------");
        Timer.benchmark(label, provider, true);
        Utils.logResult(provider.getMaxDivisorsCount(), provider.getNumbers());
    }
}
