package src.CcashApp;

public class CashTransfer {
    private CheckBalance balance;

    public CashTransfer(CheckBalance balance) {
        this.balance = balance;
    }

    public void cashTransfer(int fromUser, int toUser, double amount) {
        double senderBal = balance.getBalance(fromUser);
        double receiverBal = balance.getBalance(toUser);

        if (senderBal >= amount) {
            balance.updateBalance(fromUser, senderBal - amount);
            balance.updateBalance(toUser, receiverBal + amount);
            System.out.printf("Transfer successful! Your new balance: ₱%.2f%n", senderBal - amount);
        } else {
            System.out.println("Insufficient balance for transfer.");
        }
    }
}
