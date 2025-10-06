package src.CcashApp;

import java.util.ArrayList;
import java.util.List;

public class Transactions {

    private static class Transaction {
        int id;
        String type;
        double amount;
        int senderId;
        int receiverId;

        Transaction(int id, String type, double amount, int senderId, int receiverId) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.senderId = senderId;
            this.receiverId = receiverId;
        }
    }

    private List<Transaction> transactionList;

    public Transactions() {
        transactionList = new ArrayList<>();
    }

    public void addTransaction(String type, double amount, int senderId, int receiverId, int transactionId) {
        Transaction t = new Transaction(transactionId, type, amount, senderId, receiverId);
        transactionList.add(t);
    }

    public void showAllTransactions() {
        if (transactionList.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }

        System.out.println("\n=== Transaction History ===");
        for (Transaction t : transactionList) {
            System.out.printf(
                "ID: %d | Type: %s | Amount: %.2f | Sender: %d | Receiver: %d%n",
                t.id, t.type, t.amount, t.senderId, t.receiverId
            );
        }
    }

    public void viewUserAll(int userId) {
        boolean found = false;
        for (Transaction t : transactionList) {
            if (t.senderId == userId || t.receiverId == userId) {
                System.out.printf(
                    "ID: %d | Type: %s | Amount: %.2f | Sender: %d | Receiver: %d%n",
                    t.id, t.type, t.amount, t.senderId, t.receiverId
                );
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for this user.");
        }
    }
}
