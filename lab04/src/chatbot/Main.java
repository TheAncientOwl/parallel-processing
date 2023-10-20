package chatbot;

class Main {
    public static void main(String[] args) throws InterruptedException {
        // deadlock test
        ChatBot bot1 = new ChatBot("bot1");
        ChatBot bot2 = new ChatBot("bot2");

        bot1.setFriend(bot2);
        bot2.setFriend(bot1);

        bot1.start();
        bot2.start();

        bot1.join();
        bot2.join();
    }
}
