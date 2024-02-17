import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main class for ATM functionality
public class ATMInterface {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load user accounts from a file
        AccountManager accountManager = new AccountManager();
        accountManager.loadUserAccounts("user_accounts.csv");

        // Load transactions from a file
        TransactionsHistory transactionsHistory = new TransactionsHistory();
        transactionsHistory.loadTransactions("transactions.txt");

        // Perform user verification
        if (authenticateUser(accountManager, scanner)) {
            // Perform ATM operations
            while (true) {
                displayMenu();

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        transactionsHistory.displayHistory(accountManager.getCurrentUser().getUserId());
                        break;
                    case 2:
                        Withdraw withdraw = new Withdraw();
                        withdraw.withdrawAmount(accountManager.getCurrentUser(), transactionsHistory, accountManager);
                        break;
                    case 3:
                        Deposit deposit = new Deposit();
                        deposit.depositAmount(accountManager.getCurrentUser(), transactionsHistory, accountManager);
                        break;
                    case 4:
                        System.out.println("Enter the following details: ");
                        Transfer transfer = new Transfer();
                        transfer.transferAmount(accountManager, transactionsHistory);
                        break;
                    case 5:
                        System.out.println("Exiting ATM. Thank you!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } else {
            System.out.println("Authentication failed. Exiting ATM. Thank you!");
        }
    }

    // Function to authenticate user based on user ID and PIN
    private static boolean authenticateUser(AccountManager accountManager, Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        return accountManager.verifyCredentials(userId, pin);
    }

    // Function to display the ATM menu
    private static void displayMenu() {
        System.out.println("╔════════════════════════════════╗");
        System.out.println("║           ATM Menu             ║");
        System.out.println("╠════════════════════════════════╣");
        System.out.println("║ 1. View Transactions History   ║");
        System.out.println("║ 2. Withdraw Money              ║");
        System.out.println("║ 3. Deposit Money               ║");
        System.out.println("║ 4. Transfer Money              ║");
        System.out.println("║ 5. Quit                        ║");
        System.out.println("╚════════════════════════════════╝");
    }
    
}

// Class to manage user accounts
class AccountManager {
    private ArrayList<Account> userAccounts;
    private Account currentUser;

    public AccountManager() {
        userAccounts = new ArrayList<>();
    }

    // Load user accounts from a file
    public void loadUserAccounts(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String userId = parts[0].trim();
                    String pin = parts[1].trim();
                    double initialBalance = Double.parseDouble(parts[2].trim());

                    userAccounts.add(new Account(userId, pin, initialBalance));
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: User accounts file not found.");
        }
    }

    // Verify user credentials
    public boolean verifyCredentials(String userId, String pin) {
        for (Account account : userAccounts) {
            if (account.getUserId().equals(userId) && account.getPin().equals(pin)) {
                currentUser = account;
                return true;
            }
        }
        return false;
    }

    // Get the current user
    public Account getCurrentUser() {
        return currentUser;
    }

    // Get all user accounts
    public ArrayList<Account> getUserAccounts() {
        return userAccounts;
    }

    // Update user account balance
    public void updateBalance(Account account) {
        for (Account userAccount : userAccounts) {
            if (userAccount.getUserId().equals(account.getUserId())) {
                userAccount.setBalance(account.getBalance());
                break;
            }
        }

        // Update the balance in the user_accounts.txt file
        updateBalanceInFile();
    }

    // Update balance in the user_accounts.csv file
    private void updateBalanceInFile() {
        try {
            String filePath = "user_accounts.csv";
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                String userIdFromFile = parts[0].trim();

                if (currentUser.getUserId().equals(userIdFromFile)) {
                    // Update the balance for the current user in the line
                    parts[2] = String.format("%.2f", currentUser.getBalance());
                    lines.set(i, String.join(",", parts));
                    break;
                }
            }

            // Write the updated lines back to the file
            Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error updating balance in user_accounts.csv file.");
        }
    }
}

// Class representing a user account
class Account {
    private String userId;
    private String pin;
    private double balance;

    public Account(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
    }

    // Get user ID
    public String getUserId() {
        return userId;
    }

    // Get PIN
    public String getPin() {
        return pin;
    }

    // Get account balance
    public double getBalance() {
        return balance;
    }

    // Set account balance
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Deposit money into the account
    public void deposit(double amount) {
        balance += amount;
    }

    // Withdraw money from the account
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    // Transfer money to another account
    public void transfer(Account recipient, double amount) {
        if (amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
        } else {
            System.out.println("Insufficient funds for transfer.");
        }
    }
}

// Class to manage transaction history
class TransactionsHistory {
    private List<Transaction> transactions;

    public TransactionsHistory() {
        this.transactions = new ArrayList<>();
    }

    // Load transactions from a file
    public void loadTransactions(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    String userId = parts[0].trim();
                    String action = parts[1].trim();
                    double amount = Double.parseDouble(parts[2].trim());
                    double balance = Double.parseDouble(parts[3].trim());

                    transactions.add(new Transaction(userId, action, amount, balance));
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Transactions file not found.");
        }
    }

    // Save a transaction to the transaction history
    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);

        try {
            FileWriter fileWriter = new FileWriter("transactions.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(transaction.getUserId() + ","
                    + transaction.getAction() + ","
                    + transaction.getAmount() + ","
                    + transaction.getBalance());

            printWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to transactions file.");
        }
    }

    // Display transaction history for a specific user
    public void displayHistory(String userId) {
        System.out.println("Transaction History for " + userId + ":");
        for (Transaction transaction : transactions) {
            if (transaction.getUserId().equals(userId)) {
                System.out.println("Transaction: " + transaction.getAction()
                        + ", Amount: " + transaction.getAmount()
                        + ", Balance: " + transaction.getBalance());
            }
        }
    }
}

// Class representing a transaction
class Transaction {
    private String userId;
    private String action;
    private double amount;
    private double balance;

    public Transaction(String userId, String action, double amount, double balance) {
        this.userId = userId;
        this.action = action;
        this.amount = amount;
        this.balance = balance;
    }

    // Get user ID
    public String getUserId() {
        return userId;
    }

    // Get transaction action (e.g., deposit, withdrawal, transfer)
    public String getAction() {
        return action;
    }

    // Get transaction amount
    public double getAmount() {
        return amount;
    }

    // Get user balance after the transaction
    public double getBalance() {
        return balance;
    }
}

// Class for handling withdrawal transactions
class Withdraw {
    private Scanner scanner;

    public Withdraw() {
        this.scanner = new Scanner(System.in);
    }

    // Perform withdrawal operation
    public void withdrawAmount(Account account, TransactionsHistory transactionsHistory, AccountManager accountManager) {
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            account.withdraw(amount);
            transactionsHistory.saveTransaction(new Transaction(account.getUserId(), "Withdrawal", amount, account.getBalance()));
            accountManager.updateBalance(account);
        } else {
            System.out.println("Invalid amount.");
        }
    }
}

// Class for handling deposit transactions
class Deposit {
    private Scanner scanner;

    public Deposit() {
        this.scanner = new Scanner(System.in);
    }

    // Perform deposit operation
    public void depositAmount(Account account, TransactionsHistory transactionsHistory, AccountManager accountManager) {
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        if (amount > 0) {
            account.deposit(amount);
            transactionsHistory.saveTransaction(new Transaction(account.getUserId(), "Deposit", amount, account.getBalance()));
            accountManager.updateBalance(account);
        } else {
            System.out.println("Invalid amount.");
        }
    }
}

// Class for handling transfer transactions
class Transfer {
    private Scanner scanner;

    public Transfer() {
        this.scanner = new Scanner(System.in);
    }

    // Perform transfer operation
    public void transferAmount(AccountManager accountManager, TransactionsHistory transactionsHistory) {
        System.out.print("Enter recipient user ID: ");
        String recipientId = scanner.next();

        // Find the recipient account
        Account recipient = null;
        for (Account account : accountManager.getUserAccounts()) {
            if (account.getUserId().equals(recipientId)) {
                recipient = account;
                break;
            }
        }

        if (recipient != null) {
            System.out.print("Enter transfer amount: ");
            double amount = scanner.nextDouble();
            if (amount > 0) {
                if (accountManager.getCurrentUser().getBalance() >= amount) {
                    accountManager.getCurrentUser().transfer(recipient, amount);
                    transactionsHistory.saveTransaction(new Transaction(accountManager.getCurrentUser().getUserId(), "Transfer to " + recipient.getUserId(), amount, accountManager.getCurrentUser().getBalance()));
                    transactionsHistory.saveTransaction(new Transaction(recipient.getUserId(), "Transfer from " + accountManager.getCurrentUser().getUserId(), amount, recipient.getBalance()));
                    accountManager.updateBalance(accountManager.getCurrentUser());
                    accountManager.updateBalance(recipient);
                } else {
                    System.out.println("Insufficient funds for transfer.");
                }
            } else {
                System.out.println("Invalid amount.");
            }
        } else {
            System.out.println("Recipient not found.");
        }
    }
}