package com.example.atm.controller;

import com.example.atm.entity.*;
import com.example.atm.service.ATMService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/atm")
public class ATMController {

    private final ATMService atmService;

    public ATMController(ATMService atmService) {
        this.atmService = atmService;
    }

    // Register user
    @PostMapping("/register")
    public User register(
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam String pin
    ) {
        return atmService.registerUser(userId, name, pin);
    }

    // Login
    @GetMapping("/login")
    public User login(
            @RequestParam String userId,
            @RequestParam String pin
    ) {
        return atmService.login(userId, pin);
    }

    // Check Balance (PIN mandatory)
    @GetMapping("/balance")
    public double balance(
            @RequestParam String accountNumber,
            @RequestParam String pin
    ) {
        return atmService.checkBalance(accountNumber, pin);
    }

    // Deposit Money (PIN mandatory)
    @PostMapping("/deposit")
    public String deposit(
            @RequestParam String accountNumber,
            @RequestParam String pin,
            @RequestParam double amount
    ) {
        return atmService.deposit(accountNumber, pin, amount);
    }

    // Withdraw Money (PIN mandatory)
    @PostMapping("/withdraw")
    public String withdraw(
            @RequestParam String accountNumber,
            @RequestParam String pin,
            @RequestParam double amount
    ) {
        return atmService.withdraw(accountNumber, pin, amount);
    }

    // Transfer Money (PIN mandatory)
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String fromAccount,
            @RequestParam String pin,
            @RequestParam String toAccount,
            @RequestParam double amount
    ) {
        return atmService.transfer(fromAccount, pin, toAccount, amount);
    }

    // Transaction History (PIN mandatory)
    @GetMapping("/transactions")
    public List<Transaction> transactions(
            @RequestParam String accountNumber,
            @RequestParam String pin
    ) {
        return atmService.getHistory(accountNumber, pin);
    }
}