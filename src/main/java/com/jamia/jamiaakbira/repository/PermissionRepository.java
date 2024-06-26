package com.jamia.jamiaakbira.repository;

import com.jamia.jamiaakbira.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String permission);
}
