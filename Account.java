import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private final String cardNumber;
    private final String holderName;
    private String pin;
    private double balance;
    private final List<String> transactions;

    public Account(String cardNumber, String holderName, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        addTransaction("Account opened with balance Rs. " + format(balance));
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void changePin(String newPin) {
        pin = newPin;
        addTransaction("PIN changed successfully");
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposited Rs. " + format(amount) + " | Balance Rs. " + format(balance));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            addTransaction("Failed withdrawal of Rs. " + format(amount) + " due to insufficient balance");
            return false;
        }

        balance -= amount;
        addTransaction("Withdrawn Rs. " + format(amount) + " | Balance Rs. " + format(balance));
        return true;
    }

    public boolean transferTo(Account receiver, double amount) {
        if (amount > balance) {
            addTransaction("Failed transfer of Rs. " + format(amount) + " to " + receiver.getHolderName());
            return false;
        }

        balance -= amount;
        receiver.balance += amount;
        addTransaction("Transferred Rs. " + format(amount) + " to " + receiver.getHolderName()
                + " | Balance Rs. " + format(balance));
        receiver.addTransaction("Received Rs. " + format(amount) + " from " + holderName
                + " | Balance Rs. " + format(receiver.balance));
        return true;
    }

    public List<String> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private void addTransaction(String transaction) {
        transactions.add(transaction);
    }

    private String format(double amount) {
        return String.format("%.2f", amount);
    }
}

