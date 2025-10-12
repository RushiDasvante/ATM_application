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

    @GetMapping("/login")
    public User login(@RequestParam String userId, @RequestParam String pin) {
        return atmService.login(userId, pin);
    }

    @GetMapping("/balance/{accountNumber}")
    public double balance(@PathVariable String accountNumber) {
        return atmService.checkBalance(accountNumber);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        return atmService.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        return atmService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String from, @RequestParam String to, @RequestParam double amount) {
        return atmService.transfer(from, to, amount);
    }

    @GetMapping("/transactions/{accountNumber}")
    public List<Transaction> transactions(@PathVariable String accountNumber) {
        return atmService.getHistory(accountNumber.trim());
    }
}
