package src;

class HelloThread extends Thread {

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(">> " + System.currentTimeMillis() + " Helloooooo!");

            return;
        }
    }

}

class ByeRunnable implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(">> " + System.currentTimeMillis() + " Byeeee!");
    }

}

class Main {
    public static void main(String[] args) {
        System.out.println(">> " + System.currentTimeMillis() + " Main thread start!");

        // HelloThread helloThread = new HelloThread();
        // Thread byeThread = new Thread(new ByeRunnable());

        // helloThread.start();
        // byeThread.start();

        // try {
        // helloThread.join();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        // try {
        // byeThread.join();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        // test race conditions
        BankAccount account = new BankAccount(1000);
        BankCard husband = new BankCard("Ion", account);
        BankCard wife = new BankCard("Ioana", account);

        husband.start();
        wife.start();

        try {
            husband.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            wife.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final bank amount: " + account.getBalance());

        System.out.println(">> " + System.currentTimeMillis() + " Main thread end!");
    }

}
