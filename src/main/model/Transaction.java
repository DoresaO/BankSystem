package main.model;

public class Transaction {
    private double amount;
    private String originatingAccountId;
    private String resultingAccountId;
    private String reason;
    private double fee;

    public Transaction(double amount, String originatingAccountId, String resultingAccountId, String reason, double fee) {
        this.amount = amount;
        this.originatingAccountId = originatingAccountId;
        this.resultingAccountId = resultingAccountId;
        this.reason = reason;
        this.fee = fee;
    }

    public double getAmount() {
        return amount;
    }

    public String getOriginatingAccountId() {
        return originatingAccountId;
    }

    public String getResultingAccountId() {
        return resultingAccountId;
    }

    public String getReason() {
        return reason;
    }

    public double getFee() {
        return fee;
    }
    @Override
    public String toString() {
        return "==================================\n" +
                " Transaction Details\n" +
                "==================================\n" +
                " Amount                : $" + String.format("%.2f", amount) + "\n" +
                " Originating Account   : " + originatingAccountId + "\n" +
                " Resulting Account     : " + resultingAccountId + "\n" +
                " Reason                : " + reason + "\n" +
                " Fee                   : $" + String.format("%.2f", fee) + "\n" +
                "==================================";
    }

}