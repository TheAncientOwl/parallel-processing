package bingo;

public class Player implements Runnable {
    private String name;
    private int luckyNumber;
    BingoGame game;

    public Player(String name, int luckyNumber, BingoGame game) {
        this.name = name;
        this.luckyNumber = luckyNumber;
        this.game = game;
    }

    @Override
    public void run() {
        System.out.println("----------\n>> "
                + name
                + " joined the game with number "
                + this.luckyNumber
                + "\n----------");

        while (true) {
            // if ( this.game.getLuckyNumber() == this.luckyNumber) {
            if (this.game.getLuckyNumbers().contains(this.luckyNumber)) {
                System.err.println("----------\n>> " + this.name + " won! \\(*,*)/");
                return;
            }
        }
    }

}
