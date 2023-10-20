package bingo;

import java.util.HashSet;
import java.util.Random;

class BingoGame extends Thread {
    // @tip volatile -> it works, but not how it's supposed to
    private volatile int luckyNumber;
    private HashSet<Integer> generatedLuckyNumbers = new HashSet<>();

    @Override
    public void run() {
        System.out.println("----------\n>> Let the game begin!\n----------");

        Random random = new Random();

        while (this.generatedLuckyNumbers.size() < 100) {
            this.luckyNumber = random.nextInt(100) + 1;
            System.out.println(">> Lucky number: " + this.luckyNumber);

            this.generatedLuckyNumbers.add(this.luckyNumber);
        }

        System.out.println("----------\n>> The game ended!\n----------");
    }

    public int getLuckyNumber() {
        return this.luckyNumber;
    }

    public HashSet<Integer> getLuckyNumbers() {
        return this.generatedLuckyNumbers;
    }
}
