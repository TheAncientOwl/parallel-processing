package rest;

import java.util.concurrent.LinkedBlockingDeque;

public class MessageProcessor implements Runnable {

    private LinkedBlockingDeque<String> messages;

    public MessageProcessor(LinkedBlockingDeque<String> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(">> Extract message from the queue!");
                String message = messages.take();

                System.out.println(">> Processing message: " + message);
                if (message.equals("Message 50")) {
                    Thread.sleep(10000);
                }

                // poison-pill
                if (message.equals("end")) {
                    System.out.println(">> Message processor closed!");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
