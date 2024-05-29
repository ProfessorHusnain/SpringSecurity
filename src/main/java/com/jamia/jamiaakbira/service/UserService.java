package com.jamia.jamiaakbira.service;

import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import com.jamia.jamiaakbira.entity.Credential;
import com.jamia.jamiaakbira.entity.Role;
import com.jamia.jamiaakbira.enumeration.LoginType;

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
