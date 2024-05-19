package main.model;

import main.exception.AccountNotFoundException;
import main.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private double transactionFlatFeeAmount;
    private double transactionPercentFeeValue;
    private DB db;

    public Bank(String name, double transactionFlatFeeAmount, double transactionPercentFeeValue, DB db) {
        this.name = name;
        this.transactionFlatFeeAmount = transactionFlatFeeAmount;
        this.transactionPercentFeeValue = transactionPercentFeeValue;
        this.db = db;
    }

    public void addAccount(Account account) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO accounts(accountId, userName, balance) VALUES (?, ?, ?)");
        pstmt.setString(1, account.getAccountId());
        pstmt.setString(2, account.getUserName());
        pstmt.setDouble(3, account.getBalance());
        pstmt.executeUpdate();
    }

    public Account findAccountById(String accountId) throws AccountNotFoundException, SQLException {
        Connection connection = db.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM accounts WHERE accountId = ?");
        pstmt.setString(1, accountId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Account(rs.getString("accountId"), rs.getString("userName"), rs.getDouble("balance"));
        } else {
            throw new AccountNotFoundException("Account not found.");
        }
    }

    public List<Account> getAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        Connection connection = db.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
        while (rs.next()) {
            accounts.add(new Account(rs.getString("accountId"), rs.getString("userName"), rs.getDouble("balance")));
        }
        return accounts;
    }


    public void updateAccount(Account account) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE accountId = ?");
        pstmt.setDouble(1, account.getBalance());
        pstmt.setString(2, account.getAccountId());
        pstmt.executeUpdate();
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        Connection connection = db.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO transactions(amount, originatingAccountId, resultingAccountId, reason, fee) VALUES (?, ?, ?, ?, ?)");
        pstmt.setDouble(1, transaction.getAmount());
        pstmt.setString(2, transaction.getOriginatingAccountId());
        pstmt.setString(3, transaction.getResultingAccountId());
        pstmt.setString(4, transaction.getReason());
        pstmt.setDouble(5, transaction.getFee());
        pstmt.executeUpdate();
    }

    public List<Transaction> getTransactions(String accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = db.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM transactions WHERE originatingAccountId = ? OR resultingAccountId = ?");
        pstmt.setString(1, accountId);
        pstmt.setString(2, accountId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            transactions.add(new Transaction(rs.getDouble("amount"), rs.getString("originatingAccountId"), rs.getString("resultingAccountId"), rs.getString("reason"), rs.getDouble("fee")));
        }
        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = db.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");
        while (rs.next()) {
            transactions.add(new Transaction(rs.getDouble("amount"), rs.getString("originatingAccountId"), rs.getString("resultingAccountId"), rs.getString("reason"), rs.getDouble("fee")));
        }
        return transactions;
    }

    public double getTransactionFlatFeeAmount() {
        return transactionFlatFeeAmount;
    }

    public double getTransactionPercentFeeValue() {
        return transactionPercentFeeValue;
    }
}
