package com.jamia.jamiaakbira.utils;

import com.jamia.jamiaakbira.constant.Constant;
import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import com.jamia.jamiaakbira.entity.Credential;
import com.jamia.jamiaakbira.entity.Role;
import com.jamia.jamiaakbira.entity.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

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
        user.setAuthorities(role.getAuthorities());
        return user;
    }

    // todo: compare from database rotation

    public static boolean isCredentialsNonExpired(Credential credential) {
        return credential.getUpdatedAt().plusDays(NINETY_DAY).isAfter(LocalDateTime.now());
    }
}
