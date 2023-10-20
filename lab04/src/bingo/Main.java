package bingo;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BingoGame game = new BingoGame();

        Thread player1 = new Thread(new Player("Player1", 13, game));
        Thread player2 = new Thread(new Player("Player2", 45, game));
        Thread player3 = new Thread(new Player("Player3", 34, game));

        player1.start();
        player2.start();
        player3.start();

        game.start();

        game.join();

        player1.join();
        player2.join();
        player3.join();
    }
}
