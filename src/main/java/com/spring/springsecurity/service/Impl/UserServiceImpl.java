package com.spring.springsecurity.service.Impl;

import com.spring.springsecurity.cache.CacheStore;
import com.spring.springsecurity.domain.RequestContext;
import com.spring.springsecurity.dto.AuthenticatedUser;
import com.spring.springsecurity.entity.Confirmation;
import com.spring.springsecurity.entity.Credential;
import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.enumeration.*;
import com.spring.springsecurity.event.UserEvent;
import com.spring.springsecurity.exception.NotificationException;
import com.spring.springsecurity.repository.ConfirmationRepository;
import com.spring.springsecurity.repository.CredentialRepository;
import com.spring.springsecurity.repository.RoleRepository;
import com.spring.springsecurity.repository.UserRepository;
import com.spring.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static com.spring.springsecurity.constant.Constant.USER_NOT_FOUND;
import static com.spring.springsecurity.utils.UserUtils.createNewUserObj;
import static com.spring.springsecurity.utils.UserUtils.fromUserEntity;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final CacheStore<String, Integer> userCache;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        User user = userRepository.save(createNewUser(firstName, lastName, email));
        Credential credential = new Credential(passwordEncoder.encode(password), user);
        //Credential credential = new Credential(password, user);
        credentialRepository.save(credential);
        Confirmation confirmation = new Confirmation(user, NotificationChannel.EMAIL); // todo: manage logic for phone number verification when needed
        confirmationRepository.save(confirmation);
        publisher.publishEvent(new UserEvent(user, EventType.REGISTRATION, Map.of("key", confirmation.getKey())));
        RequestContext.start();
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotificationException("Role Not Found")); // todo: create exception class for the roles
    }

    @Override
    public void verifyAccountKey(String key) {
        Confirmation confirmation = getConfirmationByKey.apply(key);
        //User user = getUserByEmail.apply(confirmation.getUser().getEmail());
        User user = getUserByEmail(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmation);
    }

    @Override
    public void updatedLoginAttempt(String email, LoginType loginType) {
        // todo: for all type of login like phone number user id and email keep updated it.
        //User user = getUserByEmail.apply(email);
        User user = getUserByEmail(email);
        RequestContext.setUserId(user.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(user.getUserId()) == null) {
                    user.setLoginAttempts(0);
                    user.setAccountNonLocked(true);
                }
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userCache.put(user.getUserId(), user.getLoginAttempts());
                if (userCache.get(user.getUserId()) > 5) {
                    user.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                user.setLoginAttempts(0);
                user.setAccountNonLocked(true);
                user.setLastLogin(now());
                userCache.evict(user.getUserId());
            }
        }
        userRepository.save(user);
    }

    @Override
    public AuthenticatedUser getAuthenticatedUserByUserId(String userId) {
        var user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new NotificationException(USER_NOT_FOUND));
        return fromUserEntity(user, user.getRole(), getCredentialsById(user.getId()));
    }

    @Override
    public AuthenticatedUser getUserForAuthentication(String username) {
        // todo: implement the logic for the authentication for different type of login
        // todo: take look on exception class
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotificationException(USER_NOT_FOUND));
        return fromUserEntity(user, user.getRole(), getCredentialsById(user.getId()));
    }

    @Override
    public Credential getCredentialsById(Long id) {
        return credentialRepository.getCredentialByUserId(id)
                .orElseThrow(() -> new NotificationException("Credential Not Found")); //todo: excpetion
    }

    @Override
    public AuthenticatedUser authenticatedUserMetaData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return  (AuthenticatedUser) authentication.getPrincipal();
        } else {
            throw new NotificationException("User Not Authenticated");
        }
    }

    // todo: take look at the exception classes
    private final Function<String, Confirmation> getConfirmationByKey = key -> new Confirmation();/*confirmationRepository.findByKey(key)
            .orElseThrow(() -> new NotificationException("Confirmation Key Not Found"));*/

    /*  private final Function<String, User> getUserByEmail = email -> userRepository.findByEmailIgnoreCase(email)
              .orElseThrow(() -> new NotificationException("User Not Found"));*/
    private User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotificationException("User Not Found"));
    }

    private User createNewUser(String firstName, String lastName, String email) {
        var role = getRole(Roles.USER.name());
        return createNewUserObj(firstName, lastName, email, role);
    }

}
