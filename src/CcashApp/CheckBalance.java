package src.CcashApp;

import java.io.*;
import java.util.*;

public class CheckBalance {
    private static final String DB_FILE = "database.txt";
    private Map<Integer, Double> balances = new HashMap<>();

    public CheckBalance() {
        loadBalancesFromFile();
    }

    public void checkBalance(int userId) {
        double bal = balances.getOrDefault(userId, 0.0);
        System.out.printf("💰 Your current balance: ₱%.2f%n", bal);
    }

    public double getBalance(int userId) {
        return balances.getOrDefault(userId, 0.0);
    }

    public void updateBalance(int userId, double newAmount) {
        balances.put(userId, newAmount);
        saveBalancesToFile();
    }

    public void addDummyData(int userId, double amount) {
        balances.put(userId, amount);
        saveBalancesToFile();
    }

    private void loadBalancesFromFile() {
        File file = new File(DB_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");

                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    double balance = Double.parseDouble(parts[5]);
                    balances.put(id, balance);
                } else if (parts.length == 5) {
            
                    int id = Integer.parseInt(parts[0]);
                    balances.putIfAbsent(id, 0.0);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading balances: " + e.getMessage());
        }
    }

    private void saveBalancesToFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(DB_FILE))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");

                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    double balance = balances.getOrDefault(id, 0.0);
                    // Write user info + balance (6 fields)
                    lines.add(parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|" + parts[4] + "|" + balance);
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_FILE))) {
                for (String updatedLine : lines) {
                    bw.write(updatedLine);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("❌ Error saving balances: " + e.getMessage());
        }
    }
}
