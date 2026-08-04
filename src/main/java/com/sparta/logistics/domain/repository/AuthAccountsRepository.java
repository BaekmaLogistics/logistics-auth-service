package com.sparta.logistics.domain.repository;

import com.sparta.logistics.domain.entity.AuthAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthAccountsRepository extends JpaRepository<AuthAccounts, UUID> {

  boolean existsByUsername(String username);

}
