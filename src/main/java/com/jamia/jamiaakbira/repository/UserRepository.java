package com.jamia.jamiaakbira.repository;

import com.jamia.jamiaakbira.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("""
            SELECT u
            FROM User u
            WHERE lower(u.email) = lower(:email)
           """)
    Optional<User> findByEmailIgnoreCase(String email);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.userId = :userId
           """)
    Optional<User> findUserByUserId(String userId);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.phone = :phone
           """)
    Optional<User> findUserByPhone(String phone);
}
