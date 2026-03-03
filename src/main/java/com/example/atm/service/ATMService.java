package com.example.atm.service;

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
    public String login(String userId, String pin) {

        User user = userRepo.findById(userId)
                .filter(u -> passwordEncoder.matches(pin, u.getPin()))
                .orElseThrow(() -> new RuntimeException("Invalid User ID or PIN"));
        

        return jwtUtil.generateToken(user.getUserId());
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

        return "INR "+amt +"Deposit successfully. AVL bal is INR "+acc.getBalance();
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

        return "INR "+amt+" Withdraw successfully. AVL Bal is INR"+acc.getBalance();
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

        sender.setBalance(sender.getBalance() - amt);
        receiver.setBalance(receiver.getBalance() + amt);

        accountRepo.save(sender);
        accountRepo.save(receiver);

        return "INR "+amt+" is Transfered successfully.And AVL Bal is "+sender.getBalance();
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