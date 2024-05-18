package com.jamia.jamiaakbira.security;

import com.jamia.jamiaakbira.domain.ApplicationUsernamePasswordAuthenticationToken;
import com.jamia.jamiaakbira.domain.UserPrinciple;
import com.jamia.jamiaakbira.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class ApplicationAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = parseAuthentication.apply(authentication);
        var user = userService.getUserForAuthentication(auth.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Unable to authenticate");
        }

        var credentials = userService.getCredentialsById(user.getId());
        //if (credentials.getUpdatedAt().minusDays(NINETY_DAY).isAfter(LocalDateTime.now())) {
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Password has expired");
        }
        UserPrinciple userPrinciple = new UserPrinciple(user, credentials);
        validateAccount.accept(userPrinciple);
        if (passwordEncoder.matches(auth.getPassword(), credentials.getPassword())) {
            return ApplicationUsernamePasswordAuthenticationToken.authenticated(user, userPrinciple.getAuthorities());
        } else {
            throw new BadCredentialsException("provided information is incorrect");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return ApplicationUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApplicationUsernamePasswordAuthenticationToken> parseAuthentication =
            authentication -> (ApplicationUsernamePasswordAuthenticationToken) authentication;
    private final Consumer<UserPrinciple> validateAccount = userPrinciple -> {
        if (!userPrinciple.isEnabled()) {
            throw new DisabledException("You're account has been disable. Please contact the administrator");
        }
        if (!userPrinciple.isAccountNonExpired()) {
            throw new DisabledException("You're account has been expired. Please contact the administrator");
        }
        if (!userPrinciple.isAccountNonLocked()) {
            throw new LockedException("You're account is currently locked.");
        }
        if (!userPrinciple.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Your password has expired");
        }
    };
}
