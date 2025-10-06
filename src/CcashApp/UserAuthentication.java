package src.CcashApp;

import java.io.*;
import java.util.*;

public class UserAuthentication {
    private static List<User> userList = new ArrayList<>();
    private static int nextUserId = 1;
    private User loggedInUser = null;
    private static final String FILE_NAME = "database.txt";

    private CheckBalance balanceSystem;

    public UserAuthentication(CheckBalance balanceSystem) {
        this.balanceSystem = balanceSystem;
        loadUsersFromFile();
    }

    public void registerUser(Scanner sc) {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter Number: ");
        String number = sc.nextLine().trim();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Name cannot be empty.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("❌ Invalid email format.");
            return;
        }

        for (User u : userList) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("❌ Email already registered. Please use another.");
                return;
            }
        }

        if (!number.matches("^09\\d{9}$")) {
            System.out.println("❌ Invalid number. Must start with '09' and be 11 digits.");
            return;
        }

        if (!pin.matches("\\d{4}")) {
            System.out.println("❌ PIN must be exactly 4 digits.");
            return;
        }

        double initialBalance = 0.0;
        User newUser = new User(nextUserId++, name, email, number, pin);
        userList.add(newUser);
        balanceSystem.updateBalance(newUser.getId(), initialBalance);
        saveUsersToFile();
        System.out.println("✅ Registration successful for: " + name);
    }

    public boolean login(String email, String pin) {
        for (User user : userList) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPin().equals(pin)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void changePin(String oldPin, String newPin) {
        if (loggedInUser == null) {
            System.out.println("❌ No user currently logged in.");
            return;
        }

        if (!loggedInUser.getPin().equals(oldPin)) {
            System.out.println("❌ Old PIN is incorrect.");
            return;
        }

        if (!newPin.matches("\\d{4}")) {
            System.out.println("❌ New PIN must be exactly 4 digits.");
            return;
        }

        loggedInUser.setPin(newPin);
        saveUsersToFile();
        System.out.println("✅ PIN successfully changed!");
    }

    public void logout() {
        if (loggedInUser != null) {
            loggedInUser = null;
            System.out.println("✅ Successfully logged out.");
        } else {
            System.out.println("❌ No user is currently logged in.");
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : userList) {
                double bal = balanceSystem.getBalance(user.getId());
                writer.write(user.getId() + "|" + user.getName() + "|" + user.getEmail() + "|" +
                        user.getNumber() + "|" + user.getPin() + "|" + bal);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving users: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    String number = parts[3];
                    String pin = parts[4];
                    double bal = Double.parseDouble(parts[5]);

                    User user = new User(id, name, email, number, pin);
                    userList.add(user);
                    balanceSystem.updateBalance(id, bal);

                    if (id >= nextUserId) {
                        nextUserId = id + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading users: " + e.getMessage());
        }
    }
}
