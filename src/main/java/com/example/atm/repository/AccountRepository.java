package com.example.atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.atm.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String>{

}
