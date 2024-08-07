package com.spring.springsecurity.repository;

import com.spring.springsecurity.entity.Confirmation;
import com.spring.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {

    @Query("""
            SELECT c
            FROM Confirmation c
            WHERE c.key = :key
           """)
    Optional<Confirmation> findByKey(String key);

    @Query("""
            SELECT c
            FROM Confirmation c
            WHERE c.user = :user
           """)
    Optional<Confirmation> findByUser(User user);
}
