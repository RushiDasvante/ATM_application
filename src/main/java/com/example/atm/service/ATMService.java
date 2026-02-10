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
    
    @Transactional
    public User registerUser(String userId, String name, String pin) {

        // Check if user already exists
        if (userRepo.existsById(userId)) {
            throw new RuntimeException("User already exists with ID: " + userId);
        }

        // Create User
        User user = new User(userId, name, pin);

        // Auto-generate account number
        String accountNumber = "ACC" + System.currentTimeMillis();

        // Initial balance = 0
        Account account = new Account(accountNumber, 0.0, user);

        // Link both
        user.setAccount(account);

        // Save user (account saved via cascade)
        return userRepo.save(user);
    }

   
    // Login user
    public User login(String userId, String pin) {
        return userRepo.findById(userId)
                .filter(u -> u.getPin().equals(pin))
                .orElse(null);
    }

    // Check account balance
    public double checkBalance(String accountNumber) {
        Account acc = accountRepo.findById(accountNumber.trim())
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        return acc.getBalance();
    }

    // Deposit money
    @Transactional
    public String deposit(String accountNumber, double amt) {
        Account acc = accountRepo.findById(accountNumber.trim())
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        // Update account balance
        acc.setBalance(acc.getBalance() + amt);
        accountRepo.save(acc);

        // Create transaction with all details
        Transaction t = new Transaction("DEPOSIT", amt, "Deposit money", acc);
        txRepo.save(t);

        // Add to account's transaction list
        acc.getTransactions().add(t);
        accountRepo.save(acc);

        return "Deposited " + amt + " successfully.";
    }




    // Withdraw money
    @Transactional
    public String withdraw(String accountNumber, double amt) {
        Account acc = accountRepo.findById(accountNumber.trim())
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

        if (amt > acc.getBalance()) return "Insufficient balance.";

        // Update balance
        acc.setBalance(acc.getBalance() - amt);
        accountRepo.save(acc);

        // Log transaction
        Transaction t = new Transaction();
        t.setType("WITHDRAWAL");
        t.setAmount(amt);
        t.setAccount(acc);
        t.setNote("Withdraw money");
        t.setTimestamp(LocalDateTime.now());
        txRepo.save(t);

        return "Withdrawn " + amt + " successfully.";
    }

    // Transfer money
    @Transactional
    public String transfer(String fromAcc, String toAcc, double amt) {
        Account sender = accountRepo.findById(fromAcc.trim())
                .orElseThrow(() -> new RuntimeException("Sender account not found: " + fromAcc));
        Account receiver = accountRepo.findById(toAcc.trim())
                .orElseThrow(() -> new RuntimeException("Receiver account not found: " + toAcc));

        if (amt > sender.getBalance()) return "Insufficient funds.";

        // Update balances
        sender.setBalance(sender.getBalance() - amt);
        receiver.setBalance(receiver.getBalance() + amt);

        accountRepo.save(sender);
        accountRepo.save(receiver);

        // Log transactions
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

    // Transaction history
    public List<Transaction> getHistory(String accountNumber) {
        return txRepo.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber.trim());
    }
}
