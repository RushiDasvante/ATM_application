package com.example.atm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private double amount;
    private String note;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_number")
    @JsonIgnoreProperties({"transactions", "user"}) // prevent infinite recursion
    private Account account;

    // ✅ Default constructor
    public Transaction() {}

    // ✅ Convenience constructor
    public Transaction(String type, double amount, String note, Account account) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.timestamp = LocalDateTime.now();
        this.account = account;
    }

    // ✅ Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
