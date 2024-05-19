package com.jamia.jamiaakbira.utils;

import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import com.jamia.jamiaakbira.entity.Credential;
import com.jamia.jamiaakbira.entity.Permission;
import com.jamia.jamiaakbira.entity.Role;
import com.jamia.jamiaakbira.entity.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jamia.jamiaakbira.constant.Constant.NINETY_DAY;
import static java.time.LocalDateTime.now;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public class UserUtils {
    public static User createNewUserObj(String firstName, String lastName, String email, Role role){
        return User.builder()
                .userId(UUID.randomUUID().toString()) //todo: create the understandable user Id
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(EMPTY)
                .phone(EMPTY) // todo: get the phone number on the registration as well
                .bio(EMPTY)
                .imageUrl(EMPTY)
                .role(role)
                .build();
    }
    public static AuthenticatedUser fromUserEntity(User userEntity, Role role, Credential credential){
        AuthenticatedUser user=new AuthenticatedUser();
        BeanUtils.copyProperties(userEntity,user);
        user.setRole(role.getName());
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialsNonExpired(credential));
        user.setCreatedBy(userEntity.getCreatedBy());
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setUpdatedBy(userEntity.getUpdatedBy());
        //user.setAuthorities(role.getAuthorities().getValue());
        user.setAuthorities(authorities(userEntity.getRole().getPermissions())); // todo: get the authorities from the role
        return user;
    }

    private static Set<String> authorities(Set<Permission> permission) {
        return  permission.stream()
                .flatMap(p-> Stream.of(p.getName()))
                .collect(Collectors.toSet());
    }
    // todo: compare from database rotation

    public static boolean isCredentialsNonExpired(Credential credential) {
        return credential.getUpdatedAt().plusDays(NINETY_DAY).isAfter(LocalDateTime.now());
    }
}
