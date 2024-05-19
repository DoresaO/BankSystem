package main;


import java.sql.*;

public class DB {

    private static final String URL = "jdbc:mysql://127.0.0.1/BankSystem";
    private static final String USER = "sqluser";
    private static final String PASSWORD = "password";

    private Connection connection;

    public DB() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }

    public void initialize() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS transactions");
        stmt.executeUpdate("DROP TABLE IF EXISTS accounts");
        stmt.executeUpdate("CREATE TABLE accounts(accountId VARCHAR(255) PRIMARY KEY, userName VARCHAR(255), balance DOUBLE)");
        stmt.executeUpdate("CREATE TABLE transactions(transactionId INT AUTO_INCREMENT PRIMARY KEY, amount DOUBLE, originatingAccountId VARCHAR(255), resultingAccountId VARCHAR(255), reason VARCHAR(255), fee DOUBLE, FOREIGN KEY (originatingAccountId) REFERENCES accounts(accountId), FOREIGN KEY (resultingAccountId) REFERENCES accounts(accountId))");
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
