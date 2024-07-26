package com.spring.springsecurity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

@Data
public class AuthenticatedUser {
    @JsonIgnore
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;
    private String imageUrl;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private Set<String> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    private boolean credentialsNonExpired;
    private boolean mfa;
}
