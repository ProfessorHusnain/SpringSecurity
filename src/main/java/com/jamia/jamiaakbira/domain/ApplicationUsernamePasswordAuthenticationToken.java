package com.jamia.jamiaakbira.domain;

import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import com.jamia.jamiaakbira.exception.NotificationException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class ApplicationUsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {

    private static final String PASSWORD_PROTECTED = "[PASSWORD PROTECTED]";
    private static final String USERNAME_PROTECTED = "[USERNAME PROTECTED]";
    private AuthenticatedUser authenticatedUser;
    private final String password;
    private final String username;
    private boolean authenticated;


    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public ApplicationUsernamePasswordAuthenticationToken(AuthenticatedUser authenticatedUser
            , Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.authenticatedUser = authenticatedUser;
        this.password = PASSWORD_PROTECTED;
        this.username = USERNAME_PROTECTED;
        this.authenticated = true;
    }

    public ApplicationUsernamePasswordAuthenticationToken(String username, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.username = username;
        this.password = password;
        this.authenticated = false;
    }

    public static ApplicationUsernamePasswordAuthenticationToken unauthenticated(String username, String password) {
        return new ApplicationUsernamePasswordAuthenticationToken(username, password);
    }

    public static ApplicationUsernamePasswordAuthenticationToken authenticated(AuthenticatedUser authenticatedUser, Collection<? extends GrantedAuthority> authorities) {
        return new ApplicationUsernamePasswordAuthenticationToken(authenticatedUser, authorities);
    }

    @Override
    public Object getCredentials() {
        return PASSWORD_PROTECTED;
    }

    @Override
    public Object getPrincipal() {
        return this.authenticatedUser;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        // todo: make a new class for this
        throw new NotificationException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

}
