package com.example.atm.service;

import com.example.atm.entity.*;
import com.example.atm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ATMService {

    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;

    public ATMService(UserRepository userRepo, AccountRepository accountRepo, TransactionRepository txRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
    }

    // Register User
    @Transactional
    public User registerUser(String userId, String name, String pin) {

        if (userRepo.existsById(userId)) {
            throw new RuntimeException("User already exists with ID: " + userId);
        }

        User user = new User(userId, name, pin);

        String accountNumber = "ACC" + System.currentTimeMillis();

        Account account = new Account(accountNumber, 0.0, user);

        user.setAccount(account);

        return userRepo.save(user);
    }

    // Login user
    public User login(String userId, String pin) {
        return userRepo.findById(userId)
                .filter(u -> u.getPin().equals(pin))
                .orElseThrow(() -> new RuntimeException("Invalid User ID or PIN"));
    }

    // Common PIN validation for all operations
    private Account validatePin(String accountNumber, String pin) {
        Account acc = accountRepo.findById(accountNumber.trim())
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        User user = acc.getUser();

        if (!user.getPin().equals(pin)) {
            throw new RuntimeException("Invalid PIN");
        }

        return acc;
    }

    // Check account balance (PIN required)
    public double checkBalance(String accountNumber, String pin) {
        Account acc = validatePin(accountNumber, pin);
        return acc.getBalance();
    }

    // Deposit money (PIN required)
    @Transactional
    public String deposit(String accountNumber, String pin, double amt) {
        Account acc = validatePin(accountNumber, pin);
        if(amt<=0) {throw new RuntimeException("Negative Balance Exception(The Amount must be Greater Than Zero)");}
        acc.setBalance(acc.getBalance() + amt);
        accountRepo.save(acc);

        Transaction t = new Transaction();
        t.setType("DEPOSIT");
        t.setAmount(amt);
        t.setAccount(acc);
        t.setNote("Deposit money");
        t.setTimestamp(LocalDateTime.now());
        txRepo.save(t);

        return "Deposited " + amt + " successfully.";
    }

    // Withdraw money (PIN required)
    @Transactional
    public String withdraw(String accountNumber, String pin, double amt) {
        Account acc = validatePin(accountNumber, pin);
        if(amt<=0) {throw new RuntimeException("Negative Balance Exception(The Amount must be Greater Than Zero)");}

        if (amt > acc.getBalance()) {
            return "Insufficient balance.";
        }

        acc.setBalance(acc.getBalance() - amt);
        accountRepo.save(acc);

        Transaction t = new Transaction();
        t.setType("WITHDRAWAL");
        t.setAmount(amt);
        t.setAccount(acc);
        t.setNote("Withdraw money");
        t.setTimestamp(LocalDateTime.now());
        txRepo.save(t);

        return "Withdrawn " + amt + " successfully.";
    }

    // Transfer money (PIN required)
    @Transactional
    public String transfer(String fromAcc, String pin, String toAcc, double amt) {

        Account sender = validatePin(fromAcc, pin);
        
        Account receiver = accountRepo.findById(toAcc.trim())
                .orElseThrow(() -> new RuntimeException("Receiver account not found: " + toAcc));
        if(amt<=0) {throw new RuntimeException("Negative Balance Exception(The Amount must be Greater Than Zero)");}

        if (amt > sender.getBalance()) {
            return "Insufficient funds.";
        }
        if(fromAcc.equals(toAcc)) {throw new RuntimeException("Can not transfer money to the same account");}
        sender.setBalance(sender.getBalance() - amt);
        receiver.setBalance(receiver.getBalance() + amt);

        accountRepo.save(sender);
        accountRepo.save(receiver);

        Transaction t1 = new Transaction();
        t1.setType("TRANSFER_SENT");
        t1.setAmount(amt);
        t1.setAccount(sender);
        t1.setNote("To " + toAcc);
        t1.setTimestamp(LocalDateTime.now());
        txRepo.save(t1);

        Transaction t2 = new Transaction();
        t2.setType("TRANSFER_RECEIVED");
        t2.setAmount(amt);
        t2.setAccount(receiver);
        t2.setNote("From " + fromAcc);
        t2.setTimestamp(LocalDateTime.now());
        txRepo.save(t2);

        return "Transferred " + amt + " from " + fromAcc + " to " + toAcc + " successfully.";
    }

    // Transaction history (PIN required)
    public List<Transaction> getHistory(String accountNumber, String pin) {
        validatePin(accountNumber, pin);
        return txRepo.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber.trim());
    }
}