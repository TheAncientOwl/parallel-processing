package utils;

import java.util.ArrayList;

public abstract class Solution implements Runnable, ResultProvider {
    protected long maxDivisorsCount = 0;
    protected ArrayList<Integer> numbers = new ArrayList<>();

    @Override
    public long getMaxDivisorsCount() {
        return this.maxDivisorsCount;
    }

    @Override
    public ArrayList<Integer> getNumbers() {
        return this.numbers;
    }
}
