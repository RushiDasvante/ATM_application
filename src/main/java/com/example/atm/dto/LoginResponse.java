package com.example.atm.dto;

public class LoginResponse {

	private String accessToken;
    private String refreshToken;
    private String type;
    private String userId;
    private String name;
    private String accountNumber;
    private double balance;

    public LoginResponse(String accessToken, String refreshToken, String type,
                         String userId, String name,
                         String accountNumber, double balance) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.type = type;
        this.userId = userId;
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getType() {
		return type;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	
    
}