package com.example.bankingproject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserRole {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    private String role;

    @ManyToMany(mappedBy = "roles",fetch=FetchType.LAZY)
    private Set<BankUser> users;

    public UserRole() {
        users = new HashSet<BankUser>();
    }

    public UserRole(String role) {
        this.role=role;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<BankUser> getUsers() {
        return users;
    }

    public void setUsers(Set<BankUser> users) {
        this.users = users;
    }


}
