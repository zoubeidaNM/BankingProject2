package com.example.bankingproject;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min=6, max=6)
    @Column(unique=true)
    private String accountNumber;

    @NotNull
    private double balance;

    // a many to many relation ship
    @ManyToMany(mappedBy = "accounts")
    private Set<BankUser> users;

    // a many to many relation ship
    @ManyToMany
    private Set<Transaction> transactions;

    public Account() {
        balance=0.0;
        transactions = new HashSet<Transaction>();
        users = new HashSet<BankUser>();
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        transactions = new HashSet<Transaction>();
        users = new HashSet<BankUser>();
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<BankUser> getUsers() {
        return users;
    }

    public void setUsers(Set<BankUser> users) {
        this.users = users;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addUser(BankUser user){
        users.add(user);
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
}
