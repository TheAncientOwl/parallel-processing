package src;

public class BankAccount {
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return this.balance;
    }

    public synchronized void withdraw(double amount) {
        if (amount <= this.balance) {
            System.out.println(">> Withdraw amount: " + amount);
            this.balance -= amount;
            System.out.println(">> New amount: " + this.balance);
        }
    }
}
