package com.example.bankingproject;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<BankUser, Long>{
    BankUser findByUsername(String username);
}
