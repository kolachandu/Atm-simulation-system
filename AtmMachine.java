import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AtmMachine {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private final Map<String, Account> accounts;
    private final Scanner scanner;

    public AtmMachine() {
        accounts = new HashMap<>();
        scanner = new Scanner(System.in);
        seedAccounts();
    }

    public void start() {
        printHeader();

        Account loggedInAccount = login();
        if (loggedInAccount == null) {
            System.out.println("Too many invalid attempts. Card blocked for this session.");
            return;
        }

        System.out.println("\nWelcome, " + loggedInAccount.getHolderName() + "!");
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    showBalance(loggedInAccount);
                    break;
                case 2:
                    deposit(loggedInAccount);
                    break;
                case 3:
                    withdraw(loggedInAccount);
                    break;
                case 4:
                    transfer(loggedInAccount);
                    break;
                case 5:
                    showMiniStatement(loggedInAccount);
                    break;
                case 6:
                    changePin(loggedInAccount);
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void seedAccounts() {
        addAccount(new Account("1001001001", "Arjun Sharma", "1234", 25000.00));
        addAccount(new Account("2002002002", "Meera Iyer", "4321", 18500.00));
        addAccount(new Account("3003003003", "Rohan Das", "2468", 32000.00));
    }

    private void addAccount(Account account) {
        accounts.put(account.getCardNumber(), account);
    }

    private Account login() {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            String cardNumber = readText("\nEnter card number: ");
            String pin = readText("Enter PIN: ");

            Account account = accounts.get(cardNumber);
            if (account != null && account.verifyPin(pin)) {
                return account;
            }

            int remainingAttempts = MAX_LOGIN_ATTEMPTS - attempt;
            System.out.println("Invalid card number or PIN. Attempts left: " + remainingAttempts);
        }

        return null;
    }

    private void showMenu() {
        System.out.println("\n========== ATM Menu ==========");
        System.out.println("1. Balance Inquiry");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Mini Statement");
        System.out.println("6. Change PIN");
        System.out.println("7. Exit");
    }

    private void showBalance(Account account) {
        System.out.printf("Available balance: Rs. %.2f%n", account.getBalance());
    }

    private void deposit(Account account) {
        double amount = readAmount("Enter deposit amount: ");
        account.deposit(amount);
        System.out.printf("Deposit successful. New balance: Rs. %.2f%n", account.getBalance());
    }

    private void withdraw(Account account) {
        double amount = readAmount("Enter withdrawal amount: ");

        if (account.withdraw(amount)) {
            System.out.printf("Please collect your cash. New balance: Rs. %.2f%n", account.getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void transfer(Account sender) {
        String receiverCardNumber = readText("Enter receiver card number: ");

        if (sender.getCardNumber().equals(receiverCardNumber)) {
            System.out.println("You cannot transfer money to the same account.");
            return;
        }

        Account receiver = accounts.get(receiverCardNumber);
        if (receiver == null) {
            System.out.println("Receiver account not found.");
            return;
        }

        double amount = readAmount("Enter transfer amount: ");
        if (sender.transferTo(receiver, amount)) {
            System.out.printf("Transfer successful. New balance: Rs. %.2f%n", sender.getBalance());
        } else {
            System.out.println("Transfer failed due to insufficient balance.");
        }
    }

    private void showMiniStatement(Account account) {
        List<String> transactions = account.getTransactions();
        int start = Math.max(0, transactions.size() - 5);

        System.out.println("\n----- Mini Statement -----");
        for (int index = start; index < transactions.size(); index++) {
            System.out.println((index + 1) + ". " + transactions.get(index));
        }
    }

    private void changePin(Account account) {
        String currentPin = readText("Enter current PIN: ");
        if (!account.verifyPin(currentPin)) {
            System.out.println("Incorrect current PIN.");
            return;
        }

        String newPin = readText("Enter new 4-digit PIN: ");
        if (!newPin.matches("\\d{4}")) {
            System.out.println("PIN must contain exactly 4 digits.");
            return;
        }

        String confirmPin = readText("Confirm new PIN: ");
        if (!newPin.equals(confirmPin)) {
            System.out.println("PIN confirmation does not match.");
            return;
        }

        account.changePin(newPin);
        System.out.println("PIN changed successfully.");
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readAmount(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    return amount;
                }
                System.out.println("Amount must be greater than zero.");
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void printHeader() {
        System.out.println("================================");
        System.out.println("      ATM Simulation System     ");
        System.out.println("================================");
    }
}

