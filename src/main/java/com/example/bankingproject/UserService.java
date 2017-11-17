package com.example.bankingproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*public BankUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }*/


    public BankUser findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public void saveUser(BankUser user) {
        user.addRole (roleRepository.findByRole("USER"));
        user.setEnabled(true);
        userRepository.save(user);
    }
    public void saveManager(BankUser user) {
        user.addRole (roleRepository.findByRole("MANAGER"));
        user.setEnabled(true);
        userRepository.save(user);
    }
}