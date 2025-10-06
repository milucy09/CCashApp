package src.CcashApp;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class UserAuthentication {
    private static final String DB_FILE = "database.txt";
    private static List<User> userList = new ArrayList<>();
    private static int nextUserId = 1;
    private User loggedInUser = null;

    public UserAuthentication() {
        loadUsersFromFile();
    }

    public void registerUser(Scanner sc) {
        System.out.println("--- User Registration ---");
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

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            System.out.println("❌ Invalid email format.");
            return;
        }

        for (User u : userList) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("❌ Email already registered. Please use another.");
                return;
            }
        }

        if (!Pattern.matches("^09\\d{9}$", number)) {
            System.out.println("❌ Invalid number. Must start with '09' and be 11 digits.");
            return;
        }

        if (!Pattern.matches("\\d{4}", pin)) {
            System.out.println("❌ PIN must be exactly 4 digits.");
            return;
        }

        User newUser = new User(nextUserId++, name, email, number, pin);
        userList.add(newUser);
        saveUsersToFile(); // ✅ Save to file immediately
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

        if (!Pattern.matches("\\d{4}", newPin)) {
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

    private void loadUsersFromFile() {
        File file = new File(DB_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    userList.add(new User(id, parts[1], parts[2], parts[3], parts[4]));
                    if (id >= nextUserId) nextUserId = id + 1;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading users: " + e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (User u : userList) {
                bw.write(u.getId() + "|" + u.getName() + "|" + u.getEmail() + "|" +
                         u.getNumber() + "|" + u.getPin());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving users: " + e.getMessage());
        }
    }
}
