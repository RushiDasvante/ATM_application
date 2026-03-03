package com.example.atm.dto;

public class LoginResponse {

    private String token;
    private String userId;
    private String name;
    private String accountNumber;
    private double balance;

    public LoginResponse(String token, String userId, String name,
                         String accountNumber, double balance) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
}