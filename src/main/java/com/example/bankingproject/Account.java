package com.example.bankingproject;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty
    @Digits(integer=6, fraction=0)
    private String accountNumber;

    @NotNull
    @NotEmpty
    private double sold;

    // a many to many relation ship
    @ManyToMany(mappedBy = "accounts",fetch=FetchType.LAZY)
    private Set<BankUser> users;

    // a many to many relation ship
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name="account_id"),inverseJoinColumns = @JoinColumn(name="transaction_id"))
    private Set<Transaction> transactions;

    public Account() {
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

    public double getSold() {
        return sold;
    }

    public void setSold(double sold) {
        this.sold = sold;
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
