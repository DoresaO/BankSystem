package main.service;

import main.exception.AccountNotFoundException;
import main.exception.InsufficientFundsException;
import main.model.Account;
import main.model.Bank;
import main.model.Transaction;
import main.DB;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            DB db = new DB();
            db.initialize();


            Bank bank = new Bank("MyBank", 10, 5, db);
            BankService bankService = new BankService(bank);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("======================================");
                System.out.println("||        BANK MANAGEMENT MENU      ||");
                System.out.println("======================================");
                System.out.println("|| 1. Create Account                ||");
                System.out.println("|| 2. Deposit                       ||");
                System.out.println("|| 3. Withdraw                      ||");
                System.out.println("|| 4. Transfer                      ||");
                System.out.println("|| 5. List Transactions             ||");
                System.out.println("|| 6. Check Balance                 ||");
                System.out.println("|| 7. List All Accounts             ||");
                System.out.println("|| 8. Check Total Transaction Fees  ||");
                System.out.println("|| 9. Check Total Transfer Amounts  ||");
                System.out.println("|| 10. Exit to Main Menu            ||");
                System.out.println("======================================");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                try {
                    switch (choice) {
                        case 1:
                            System.out.print("Enter username: ");
                            String userName = scanner.nextLine();
                            System.out.print("Enter initial balance: ");
                            double initialBalance = scanner.nextDouble();
                            Account account = bankService.createAccount(userName, initialBalance);
                            System.out.println("Account created with ID: " + account.getAccountId());
                            break;

                        case 2:
                            System.out.print("Enter account ID: ");
                            String depositAccountId = scanner.nextLine();
                            System.out.print("Enter deposit amount: ");
                            double depositAmount = scanner.nextDouble();
                            bankService.deposit(depositAccountId, depositAmount);
                            System.out.println("Deposit successful.");
                            break;

                        case 3:
                            System.out.print("Enter account ID: ");
                            String withdrawAccountId = scanner.nextLine();
                            System.out.print("Enter withdrawal amount: ");
                            double withdrawAmount = scanner.nextDouble();
                            bankService.withdraw(withdrawAccountId, withdrawAmount);
                            System.out.println("Withdrawal successful.");
                            break;

                        case 4:
                            System.out.print("Enter from account ID: ");
                            String fromAccountId = scanner.nextLine();
                            System.out.print("Enter to account ID: ");
                            String toAccountId = scanner.nextLine();
                            System.out.print("Enter transfer amount: ");
                            double transferAmount = scanner.nextDouble();
                            System.out.print("Use flat fee? (true/false): ");
                            boolean isFlatFee = scanner.nextBoolean();
                            bankService.transfer(fromAccountId, toAccountId, transferAmount, isFlatFee);
                            System.out.println("Transfer successful.");
                            break;

                        case 5:
                            System.out.print("Enter account ID: ");
                            String transactionAccountId = scanner.nextLine();
                            List<Transaction> transactions = bankService.getTransactions(transactionAccountId);
                            for (Transaction transaction : transactions) {
                                System.out.println(transaction);
                            }
                            break;

                        case 6:
                            System.out.print("Enter account ID: ");
                            String balanceAccountId = scanner.nextLine();
                            double balance = bankService.checkBalance(balanceAccountId);
                            System.out.println("Account balance: $" + balance);
                            break;

                        case 7:
                            List<Account> accounts = bankService.getAllAccounts();
                            for (Account acc : accounts) {
                                System.out.println(acc);
                            }
                            break;

                        case 8:
                            double totalTransactionFeeAmount = bankService.getTotalTransactionFeeAmount();
                            System.out.println("Total transaction fee amount: $" + totalTransactionFeeAmount);
                            break;

                        case 9:
                            double totalTransferAmount = bankService.getTotalTransferAmount();
                            System.out.println("Total transfer amount: $" + totalTransferAmount);
                            break;

                        case 10:
                            System.out.println("Exiting...");
                            db.close();
                            return;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (AccountNotFoundException | InsufficientFundsException | SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
