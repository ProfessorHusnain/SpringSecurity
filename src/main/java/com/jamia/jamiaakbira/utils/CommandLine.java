package com.jamia.jamiaakbira.utils;

import com.jamia.jamiaakbira.domain.RequestContext;
import com.jamia.jamiaakbira.entity.Permission;
import com.jamia.jamiaakbira.entity.Role;
import com.jamia.jamiaakbira.enumeration.Permissions;
import com.jamia.jamiaakbira.enumeration.Roles;
import com.jamia.jamiaakbira.repository.PermissionRepository;
import com.jamia.jamiaakbira.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class CommandLine {
    /*@Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        return args -> {
            RequestContext.setUserId(0L);


            for (Permissions permission : Permissions.values()) {
                var permissionObject = new Permission();
                permissionObject.setName(permission.getPermission());
                permissionRepository.save(permissionObject);
            }

            for (Roles role : Roles.values()) {
                var roleObject = new Role();
                roleObject.setName(role.getRole());
                roleRepository.save(roleObject);
            }


            for (Permissions permissions : Permissions.values()) {
                var permissionObject = permissionRepository.findByName(permissions.getPermission());
                if (
                        Objects.equals(permissionObject.getName(), Permissions.USER_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.USER_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.USER_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.USER_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PERMISSION_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PERMISSION_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PERMISSION_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PERMISSION_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.ROLE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.ROLE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.ROLE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.ROLE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_DELETE.getPermission())

                ) {
                    var roleObject = roleRepository.findByName(Roles.SUPER_ADMIN.getRole());
                    if (roleObject != null) {
                        roleObject.getPermissions().add(permissionObject);
                        roleRepository.save(roleObject);
                    }
                }
                if (
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_DELETE.getPermission())
                ) {
                    var roleObject = roleRepository.findByName(Roles.ADMIN.getRole());
                    if (roleObject != null) {
                        roleObject.getPermissions().add(permissionObject);
                        roleRepository.save(roleObject);
                    }
                }
                if (
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.DEVOTEE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_DELETE.getPermission())
                ) {
                    var roleObject = roleRepository.findByName(Roles.MANAGER.getRole());
                    if (roleObject != null) {
                        roleObject.getPermissions().add(permissionObject);
                        roleRepository.save(roleObject);
                    }
                }
                if (
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_DELETE.getPermission())

                ) {
                    var roleObject = roleRepository.findByName(Roles.TEACHER.getRole());
                    if (roleObject != null) {
                        roleObject.getPermissions().add(permissionObject);
                        roleRepository.save(roleObject);
                    }
                }
                if (
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_CREATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_DELETE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_UPDATE.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.PROFILE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.MESSAGE_VIEW.getPermission()) ||
                        Objects.equals(permissionObject.getName(), Permissions.LOGS_VIEW.getPermission())
                ) {
                    var roleObject = roleRepository.findByName(Roles.USER.getRole());
                    if (roleObject != null) {
                        roleObject.getPermissions().add(permissionObject);
                        roleRepository.save(roleObject);
                    }
                }
            }

            RequestContext.start();
        };
    }*/
}
