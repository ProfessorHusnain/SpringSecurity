package com.spring.springsecurity.repository;

import com.spring.springsecurity.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential,Long>{
    Optional<Credential> getCredentialByUserId(Long userId);
}
