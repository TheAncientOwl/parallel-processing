import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final int ARRAY_SIZE = (int) (1e8);
    private static final int BASE = 2;

    public static List<Integer> generateArray(int size) {
        ArrayList<Integer> arr = new ArrayList<>();

        for (int i = 0; i < size; i += 1) {
            arr.add(i + 1);
        }

        return arr;
    }

    public static void main(String[] args) {
        System.out.println(">> Generating data...");
        ArrayList<Integer> nums = (ArrayList<Integer>) generateArray(ARRAY_SIZE);
        System.out.println(">> Generated data");

        { // test sequential solution
            long startTime = System.currentTimeMillis();
            int result = SequentialSolution.getMultiplesCount(nums, BASE, 50);
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Rezultatul este %d, obtinut in %f secunde", result,
                    (float) (endTime - startTime) / 1000));
        }

        { // test parallel solution without race condition
            long startTime = System.currentTimeMillis();
            int result = ParalellSolution.getMultiplesCountWithoutRaceCondition(nums, BASE, 50);
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Rezultatul este %d, obtinut in %f secunde", result,
                    (float) (endTime - startTime) / 1000));
        }

        { // test parallel solution
            long startTime = System.currentTimeMillis();
            Result result = ParalellSolution.getMultiplesCount(nums, BASE, 50);
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Rezultatul este %d, obtinut in %f secunde",
                    result.getValue(),
                    (float) (endTime - startTime) / 1000));
        }
    }
}
