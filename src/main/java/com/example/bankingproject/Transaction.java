package com.example.bankingproject;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty
    @Digits(integer=6, fraction=0)
    private String accountNumber;

    @NotNull
    @NotEmpty
    private String action;


    @NotNull
    @NotEmpty
    @Digits(integer=20, fraction=2)
    private double amount;

    private String description;

    @NotNull
    @NotEmpty
    @DateTimeFormat(pattern = "MM/dd/YYYY")
    private String date;

    // a many to many relation ship
    @ManyToMany(mappedBy = "transactions",fetch=FetchType.LAZY)
    private Set<Account> accounts;

    public Transaction() {
        accounts = new HashSet<Account>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }
}
