package com.example.atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.atm.entity.*;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {}