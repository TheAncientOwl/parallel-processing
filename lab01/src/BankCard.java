package src;

import java.util.Random;

public class BankCard extends Thread {
    private String owner;
    BankAccount account;

    public BankCard(String owner, BankAccount account) {
        this.owner = owner;
        this.account = account;
    }

    public void run() {
        while (true) {
            if (this.account.getBalance() <= 0) {
                return;
            }
            Random random = new Random();
            double withdrawSum = random.nextInt(50);
            System.out.println(">> " + this.owner + " tries to withdraw " + withdrawSum
                    + ", account balance: " + this.account.getBalance());
            this.account.withdraw(withdrawSum);

            // try {
            // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
        }
    }
}
