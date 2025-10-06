package src.CcashApp;

import java.util.Scanner;

public class CcashApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserAuthentication auth = new UserAuthentication();
        CheckBalance balance = new CheckBalance();
        Cashin cashin = new Cashin(balance);
        CashTransfer transfer = new CashTransfer(balance);
        Transactions trans = new Transactions();

        boolean running = true;

        while (running) {
            System.out.println("==============================");
            System.out.println("     WELCOME TO CCASH APP     ");
            System.out.println("==============================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("❌ Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.println("\n--- User Login ---");
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter PIN: ");
                    String pin = sc.nextLine();

                    if (auth.login(email, pin)) {
                        int userId = auth.getLoggedInUser().getId();
                        String userName = auth.getLoggedInUser().getName();
                        System.out.println("✅ Login successful! Welcome " + userName + ".");

                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n==============================");
                            System.out.println("          MAIN MENU           ");
                            System.out.println("==============================");
                            System.out.println("1. Check Balance");
                            System.out.println("2. Cash-in");
                            System.out.println("3. Transfer");
                            System.out.println("4. View Transactions");
                            System.out.println("5. Change PIN");
                            System.out.println("6. Logout");
                            System.out.print("Enter your choice: ");

                            if (!sc.hasNextInt()) {
                                System.out.println("❌ Invalid input. Please enter a number.");
                                sc.nextLine();
                                continue;
                            }

                            int action = sc.nextInt();
                            sc.nextLine();

                            switch (action) {
                                case 1:
                                    balance.checkBalance(userId);
                                    break;

                                case 2:
                                    System.out.println("--- Cash In ---");
                                    System.out.print("Enter amount to cash in: ");
                                    if (sc.hasNextDouble()) {
                                        double cashInAmt = sc.nextDouble();
                                        sc.nextLine();
                                        cashin.cashin(userId, cashInAmt);
                                        trans.addTransaction("Cash-In", cashInAmt, userId, 0, 0);
                                    } else {
                                        System.out.println("❌ Invalid amount!");
                                        sc.nextLine();
                                    }
                                    break;

                                case 3:
                                    System.out.println("--- Cash Transfer ---");
                                    System.out.print("Enter recipient user ID: ");
                                    if (!sc.hasNextInt()) {
                                        System.out.println("❌ Invalid user ID!");
                                        sc.nextLine();
                                        break;
                                    }
                                    int toUser = sc.nextInt();

                                    System.out.print("Enter amount to transfer: ");
                                    if (!sc.hasNextDouble()) {
                                        System.out.println("❌ Invalid amount!");
                                        sc.nextLine();
                                        break;
                                    }
                                    double amt = sc.nextDouble();
                                    sc.nextLine();

                                    transfer.cashTransfer(userId, toUser, amt);
                                    trans.addTransaction("Transfer", amt, userId, toUser, userId);
                                    break;

                                case 4:
                                    trans.viewUserAll(userId);
                                    break;

                                case 5:
                                    System.out.print("Enter old PIN: ");
                                    String oldPin = sc.nextLine();
                                    System.out.print("Enter new PIN: ");
                                    String newPin = sc.nextLine();
                                    auth.changePin(oldPin, newPin);
                                    break;

                                case 6:
                                    auth.logout();
                                    loggedIn = false;
                                    System.out.println("👋 Goodbye " + userName + "!");
                                    break;

                                default:
                                    System.out.println("❌ Invalid option!");
                            }
                        }
                    } else {
                        System.out.println("❌ Invalid email or PIN.");
                    }
                    break;

                case 2:
                    auth.registerUser(sc);
                    break;

                case 3:
                    running = false;
                    System.out.println("👋 Exiting CCashApp...");
                    break;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }

        sc.close();
    }
}
