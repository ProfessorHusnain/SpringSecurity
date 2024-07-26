package com.spring.springsecurity.service;

import com.spring.springsecurity.dto.AuthenticatedUser;
import com.spring.springsecurity.entity.Credential;
import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName,String lastName,String email,String password);
    Role getRole(String name);
    void verifyAccountKey(String key);
    void updatedLoginAttempt(String email, LoginType loginType);

    AuthenticatedUser getAuthenticatedUserByUserId(String userId);

    AuthenticatedUser getUserForAuthentication(String username);

    Credential getCredentialsById(Long id);

    AuthenticatedUser authenticatedUserMetaData();
}
