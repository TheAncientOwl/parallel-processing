package utils;

import java.util.ArrayList;

public class Utils {
    public static void logResult(long max, ArrayList<Integer> nums) {
        System.out.print(">> Max: " + max + " -> Nums: ");
        for (var num : nums) {
            System.out.print(num.toString() + ", ");
        }
        System.out.println("");
    }
}
