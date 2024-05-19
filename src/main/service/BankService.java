package main.service;

import main.exception.AccountNotFoundException;
import main.exception.InsufficientFundsException;
import main.model.Account;
import main.model.Bank;
import main.model.Transaction;


import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class BankService {
    private Bank bank;

    public BankService(Bank bank) {
        this.bank = bank;
    }

    public Account createAccount(String userName, double initialBalance) throws SQLException {
        String accountId = UUID.randomUUID().toString();
        Account newAccount = new Account(accountId, userName, initialBalance);
        bank.addAccount(newAccount);
        return newAccount;
    }


    public void deposit(String accountId, double amount) throws AccountNotFoundException, SQLException {
        Account account = bank.findAccountById(accountId);
        account.deposit(amount);
        bank.updateAccount(account);
        bank.addTransaction(new Transaction(amount, accountId, accountId, "Deposit", 0));
    }

    public void withdraw(String accountId, double amount) throws AccountNotFoundException, InsufficientFundsException, SQLException {
        Account account = bank.findAccountById(accountId);
        account.withdraw(amount);
        bank.updateAccount(account);
        bank.addTransaction(new Transaction(amount, accountId, accountId, "Withdrawal", 0));
    }

    public void transfer(String fromAccountId, String toAccountId, double amount, boolean isFlatFee) throws AccountNotFoundException, InsufficientFundsException, SQLException {
        Account fromAccount = bank.findAccountById(fromAccountId);
        Account toAccount = bank.findAccountById(toAccountId);

        double fee = isFlatFee ? bank.getTransactionFlatFeeAmount() : amount * bank.getTransactionPercentFeeValue() / 100;

        if (fromAccount.getBalance() < amount + fee) {
            throw new InsufficientFundsException("Insufficient funds for transfer.");
        }

        fromAccount.withdraw(amount + fee);
        toAccount.deposit(amount);
        bank.updateAccount(fromAccount);
        bank.updateAccount(toAccount);
        bank.addTransaction(new Transaction(amount, fromAccountId, toAccountId, "Transfer", fee));
    }

    public List<Transaction> getTransactions(String accountId) throws SQLException {
        return bank.getTransactions(accountId);
    }

    public double checkBalance(String accountId) throws AccountNotFoundException, SQLException {
        return bank.findAccountById(accountId).getBalance();
    }

    public List<Account> getAllAccounts() throws SQLException {
        return bank.getAccounts();
    }

    public double getTotalTransactionFeeAmount() throws SQLException {
        double totalFee = 0;
        List<Transaction> allTransactions = bank.getAllTransactions();
        for (Transaction transaction : allTransactions) {
            totalFee += transaction.getFee();
        }
        return totalFee;
    }

    public double getTotalTransferAmount() throws SQLException {
        double totalTransfer = 0;
        List<Transaction> allTransactions = bank.getAllTransactions();
        for (Transaction transaction : allTransactions) {
            if ("Transfer".equals(transaction.getReason())) {
                totalTransfer += transaction.getAmount();
            }
        }
        return totalTransfer;
    }
}
