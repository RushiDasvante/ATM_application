package com.example.atm.controller;

import com.example.atm.entity.*;
import com.example.atm.service.ATMService;
import com.example.atm.dto.*;
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
    public User register(@RequestBody RegisterRequest request) {
        return atmService.registerUser(
                request.getUserId(),
                request.getName(),
                request.getPin()
        );
    }

    // Login
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return atmService.login(
                request.getUserId(),
                request.getPin()
        );
    }

    // Check Balance
    @PostMapping("/balance")
    public double balance(@RequestBody BalanceRequest request) {
        return atmService.checkBalance(
                request.getAccountNumber(),
                request.getPin()
        );
    }

    // Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestBody DepositRequest request) {
        return atmService.deposit(
                request.getAccountNumber(),
                request.getPin(),
                request.getAmount()
        );
    }

    // Withdraw
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody WithdrawRequest request) {
        return atmService.withdraw(
                request.getAccountNumber(),
                request.getPin(),
                request.getAmount()
        );
    }

    // Transfer
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        return atmService.transfer(
                request.getFromAccount(),
                request.getPin(),
                request.getToAccount(),
                request.getAmount()
        );
    }

    // Transaction history
    @PostMapping("/transactions")
    public List<Transaction> transactions(@RequestBody BalanceRequest request) {
        return atmService.getHistory(
                request.getAccountNumber(),
                request.getPin()
        );
    }
}