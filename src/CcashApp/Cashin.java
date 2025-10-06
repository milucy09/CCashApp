package src.CcashApp;

public class Cashin {
    private CheckBalance balance;

    public Cashin(CheckBalance balance) {
        this.balance = balance;
    }

    public void cashin(int userId, double amount) {
        double current = balance.getBalance(userId);
        balance.updateBalance(userId, current + amount);
        System.out.printf("Cash-in successful! Your new balance: ₱%.2f%n", current + amount);
    }
}
