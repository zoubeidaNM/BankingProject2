package com.example.bankingproject;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RoleRepository extends CrudRepository<UserRole, Long>{
    UserRole findByRole(String role);
}
