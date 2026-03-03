package com.example.atm.dto;

public class TransferRequest {
    private String fromAccount;
    private String toAccount;
   // private String pin;
    private double amount;

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

   

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}