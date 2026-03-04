package com.example.atm.service;

import com.example.atm.dto.LoginResponse;
import com.example.atm.entity.*;
import com.example.atm.repository.*;
import com.example.atm.security.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class ATMService {

    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ATMService(UserRepository userRepo,
                      AccountRepository accountRepo,
                      TransactionRepository txRepo,
                      PasswordEncoder passwordEncoder,
                      JwtUtil jwtUtil) {

        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil; // FIXED
    }

    // Register User
    @Transactional
    public User registerUser(String userId, String name, String pin) {

        if (userRepo.existsById(userId)) {
            throw new RuntimeException("User already exists with ID: " + userId);
        }

        String hashedPin = passwordEncoder.encode(pin);
        User user = new User(userId, name, hashedPin);

        String accountNumber = "ACC" + System.currentTimeMillis();
        Account account = new Account(accountNumber, 0.0, user);

        user.setAccount(account);

        return userRepo.save(user);
    }

    // Login
//    public LoginResponse login(String userId, String pin) {
//
//        User user = userRepo.findById(userId)
//                .filter(u -> passwordEncoder.matches(pin, u.getPin()))
//                .orElseThrow(() -> new RuntimeException("Invalid User ID or PIN"));
//
//        String token = jwtUtil.generateToken(user.getUserId());
//
//        Account account = user.getAccount();
//
//        return new LoginResponse(
//                token,
//                user.getUserId(),
//                user.getName(),
//                account.getAccountNumber(),
//                account.getBalance()
//        );
//    }
    public LoginResponse login(String userId, String pin) {

        User user = userRepo.findById(userId)
                .filter(u -> passwordEncoder.matches(pin, u.getPin()))
                .orElseThrow(() -> new RuntimeException("Invalid User ID or PIN"));

        // Generate Access Token (short expiry)
        String accessToken = jwtUtil.generateAccessToken(user.getUserId());

        // Generate Refresh Token (long expiry)
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        Account account = user.getAccount();

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer",
                user.getUserId(),
                user.getName(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }
    public LoginResponse refreshToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtUtil.generateAccessToken(userId);

        Account account = user.getAccount();

        return new LoginResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                user.getUserId(),
                user.getName(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }

    private Account getUserAccount(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getAccount();
    }

    // Check Balance
    public double checkBalance(String userId) {
        return getUserAccount(userId).getBalance();
    }

    // Deposit
    @Transactional
    public String deposit(String userId, double amt) {

        if (amt <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Account acc = getUserAccount(userId);

        acc.setBalance(acc.getBalance() + amt);
        accountRepo.save(acc);

        Transaction t = new Transaction("DEPOSIT", amt, "Deposit", acc);
        txRepo.save(t);

        return "INR "+amt +" Deposit successfully. AVL bal is INR "+acc.getBalance();
    }
    // Withdraw
    @Transactional
    public String withdraw(String userId, double amt) {

        Account acc = getUserAccount(userId);

        if (amt <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        if (amt > acc.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amt);
        accountRepo.save(acc);

        Transaction t = new Transaction("WITHDRAWAL", amt, "Withdraw", acc);
        txRepo.save(t);

        return "INR "+amt+" Withdraw successfully. AVL Bal is INR " +acc.getBalance();
    }

    // Transfer
    @Transactional
    public String transfer(String userId, String toAccount, double amt) {

        Account sender = getUserAccount(userId);

        Account receiver = accountRepo.findById(toAccount)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (amt <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        if (amt > sender.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        if (sender.getAccountNumber().equals(toAccount)) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        // Update balances
        sender.setBalance(sender.getBalance() - amt);
        receiver.setBalance(receiver.getBalance() + amt);

        accountRepo.save(sender);
        accountRepo.save(receiver);

        // Save transaction for sender
        Transaction t1 = new Transaction();
        t1.setType("TRANSFER_SENT");
        t1.setAmount(amt);
        t1.setAccount(sender);
        t1.setNote("To " + receiver.getAccountNumber());
        t1.setTimestamp(LocalDateTime.now());
        txRepo.save(t1);

        // Save transaction for receiver
        Transaction t2 = new Transaction();
        t2.setType("TRANSFER_RECEIVED");
        t2.setAmount(amt);
        t2.setAccount(receiver);
        t2.setNote("From " + sender.getAccountNumber());
        t2.setTimestamp(LocalDateTime.now());
        txRepo.save(t2);

        return "INR " + amt + " transferred successfully. Available Balance: " + sender.getBalance();
    }
    public List<Transaction> getHistory(String userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = user.getAccount();

        return txRepo.findByAccount_AccountNumberOrderByTimestampDesc(
                account.getAccountNumber()
        );
    }
}