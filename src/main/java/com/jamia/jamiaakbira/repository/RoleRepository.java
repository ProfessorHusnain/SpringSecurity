package com.jamia.jamiaakbira.repository;

import com.jamia.jamiaakbira.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByNameIgnoreCase(String name);

    Role findByName(String role);
}
