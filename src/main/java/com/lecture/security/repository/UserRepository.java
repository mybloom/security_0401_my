package com.lecture.security.repository;

import com.lecture.security.domain.entitiy.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
