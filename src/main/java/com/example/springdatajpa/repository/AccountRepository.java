package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByEmail(String email);
}
