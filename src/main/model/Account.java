package main.model;

import main.exception.InsufficientFundsException;

public class Account {
    private String accountId;
    private String userName;
    private double balance;

    public Account(String accountId, String userName, double balance) {
        this.accountId = accountId;
        this.userName = userName;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUserName() {
        return userName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        balance -= amount;
    }
    @Override
    public String toString() {
        return "==================================\n" +
                " Account Details\n" +
                "==================================\n" +
                " Account ID   : " + accountId + "\n" +
                " User Name    : " + userName + "\n" +
                " Balance      : $" + String.format("%.2f", balance) + "\n" +
                "==================================";
    }


    public void setBalance(double balance) {
        this.balance = balance;
    }}
