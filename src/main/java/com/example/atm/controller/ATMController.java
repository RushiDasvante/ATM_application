package com.example.atm.controller;

import com.example.atm.entity.*;
import com.example.atm.service.ATMService;
import com.example.atm.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/atm")
public class ATMController {

    private final ATMService atmService;

    public ATMController(ATMService atmService) {
        this.atmService = atmService;
    }

    // Register (Public)
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return atmService.registerUser(
                request.getUserId(),
                request.getName(),
                request.getPin()
        );
    }

    // Login (Public) -> returns JWT token
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return atmService.login(
                request.getUserId(),
                request.getPin()
        );
    }
    

    // Check Balance (JWT Required)
    @GetMapping("/balance")
    public double balance(HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return atmService.checkBalance(userId);
    }

    // Deposit
    @PostMapping("/deposit")
    public String deposit(HttpServletRequest request,
                          @RequestBody DepositRequest req) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        if (req.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        return atmService.deposit(
                userId,
                req.getAmount()
        );
    }

    // Withdraw
    @PostMapping("/withdraw")
    public String withdraw(HttpServletRequest request,
                           @RequestBody WithdrawRequest req) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        if (req.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        return atmService.withdraw(
                userId,
                req.getAmount()
        );
    }

    // Transfer
    @PostMapping("/transfer")
    public String transfer(HttpServletRequest request,
                           @RequestBody TransferRequest req) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        if (req.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }
        
        return atmService.transfer(
                userId,
                req.getToAccount(),
                req.getAmount()
        );
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions(HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return atmService.getHistory(userId);
    }
}