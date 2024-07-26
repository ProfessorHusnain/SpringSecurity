package com.spring.springsecurity.security;

import com.spring.springsecurity.constant.Constant;
import com.spring.springsecurity.domain.ApplicationUsernamePasswordAuthenticationToken;
import com.spring.springsecurity.domain.UserPrinciple;
import com.spring.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.spring.springsecurity.constant.Constant.*;

@RequiredArgsConstructor
public class ApplicationAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = parseAuthentication.apply(authentication);
        var user = userService.getUserForAuthentication(auth.getUsername());
        if (user == null) {
            throw new BadCredentialsException(USER_NOT_FOUND_IN_SECURITY);
        }

        var credentials = userService.getCredentialsById(user.getId());
        //if (credentials.getUpdatedAt().minusDays(NINETY_DAY).isAfter(LocalDateTime.now())) {
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(Constant.PASSWORD_EXPIRED);
        }
        UserPrinciple userPrinciple = new UserPrinciple(user, credentials);
        validateAccount.accept(userPrinciple);
        if (passwordEncoder.matches(auth.getPassword(), credentials.getPassword())) {
            return ApplicationUsernamePasswordAuthenticationToken.authenticated(user, userPrinciple.getAuthorities());
        } else {
            throw new BadCredentialsException(PASSWORD_IS_NOT_CORRECT);
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
            throw new DisabledException(ACCOUNT_DISABLED);
        }
        if (!userPrinciple.isAccountNonExpired()) {
            throw new DisabledException(ACCOUNT_EXPIRED);
        }
        if (!userPrinciple.isAccountNonLocked()) {
            throw new LockedException(ACCOUNT_LOCKED);
        }
        if (!userPrinciple.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(PASSWORD_EXPIRED);
        }
    };
}
