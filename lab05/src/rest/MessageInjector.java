package rest;

import java.util.concurrent.LinkedBlockingDeque;

public class MessageInjector implements Runnable {

    private LinkedBlockingDeque<String> messages;

    public MessageInjector(LinkedBlockingDeque<String> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(200);
                System.out.println(">> Insert message: " + (i + 1));

                this.messages.put("Message " + (i + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(">> Insert poison pill!");
        try {
            this.messages.put("end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
