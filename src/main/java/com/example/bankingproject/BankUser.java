package com.example.bankingproject;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BankUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private String email;

    @NotNull
    @NotEmpty
    @Size(min=4, max=20)
    private String password;

    @NotNull
    @NotEmpty
    @Size(min=2, max=30)
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(min=2, max=30)
    private String firstName;

    @NotNull
   private String accountNumber;


    private boolean enabled;

    @NotNull
    @NotEmpty
    @Size(min=2, max=20)
    @Column(unique=true)
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<UserRole> roles;

    @ManyToMany
     private Set<Account> accounts;

    public BankUser() {
        accountNumber="";
        roles = new HashSet<UserRole>();
        accounts = new HashSet<Account>();
    }

    public void setAccountNumber(String accountNumber) {
        if(!accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        }
    }

    public void createAccount(){
        Account account = new Account(accountNumber);
        addAccount(account);
    }

    public void addAccount(Account account){
        accounts.add(account);
    }


    public void addRole(UserRole role){
        roles.add(role);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public String getAccountNumber() {
        return accountNumber;
    }



}
