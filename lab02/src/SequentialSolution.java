import java.util.ArrayList;

class SequentialSolution {
    public static int getMultiplesCount(ArrayList<Integer> nums, int base, int iterationsCount) {
        int out = 0;

        for (int j = 0; j < iterationsCount; j += 1) {
            for (int i = 0; i < nums.size(); i += 1) {
                if (nums.get(i) % base == 0) {
                    out += 1;
                }
            }
        }

        return out;
    }
}
