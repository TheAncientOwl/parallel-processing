package chatbot;

public class ChatBot extends Thread {
    private String name;
    private ChatBot friend;

    public ChatBot(String name) {
        this.name = name;
        this.friend = null;
    }

    public void setFriend(ChatBot friend) {
        this.friend = friend;
    }

    public synchronized void hello(ChatBot bot) {
        System.out.println(this.name + " says hello to " + bot.name);
        bot.answer(this);
    }

    public synchronized void answer(ChatBot bot) {
        System.out.println(this.name + " answers to " + bot.name);
    }

    @Override
    public void run() {
        this.hello(friend);
    }
}
