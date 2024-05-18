package com.jamia.jamiaakbira.service.Impl;

import com.jamia.jamiaakbira.cache.CacheStore;
import com.jamia.jamiaakbira.domain.RequestContext;
import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import com.jamia.jamiaakbira.entity.Confirmation;
import com.jamia.jamiaakbira.entity.Credential;
import com.jamia.jamiaakbira.entity.Role;
import com.jamia.jamiaakbira.entity.User;
import com.jamia.jamiaakbira.enumeration.Authority;
import com.jamia.jamiaakbira.enumeration.EventType;
import com.jamia.jamiaakbira.enumeration.LoginType;
import com.jamia.jamiaakbira.event.UserEvent;
import com.jamia.jamiaakbira.exception.NotificationException;
import com.jamia.jamiaakbira.repository.ConfirmationRepository;
import com.jamia.jamiaakbira.repository.CredentialRepository;
import com.jamia.jamiaakbira.repository.RoleRepository;
import com.jamia.jamiaakbira.repository.UserRepository;
import com.jamia.jamiaakbira.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static com.jamia.jamiaakbira.constant.Constant.USER_NOT_FOUND;
import static com.jamia.jamiaakbira.utils.UserUtils.createNewUserObj;
import static com.jamia.jamiaakbira.utils.UserUtils.fromUserEntity;
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
        Confirmation confirmation = new Confirmation(user); // todo: manage logic for phone number verification when needed
        confirmationRepository.save(confirmation);
        publisher.publishEvent(new UserEvent(user, EventType.REGISTRATION, Map.of("key", confirmation.getKey())));
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
        return fromUserEntity(user,user.getRole(),getCredentialsById(user.getId()));
    }

    @Override
    public AuthenticatedUser getUserForAuthentication(String username) {
        // todo: implement the logic for the authentication for different type of login
        // todo: take look on exception class
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotificationException(USER_NOT_FOUND));
        return fromUserEntity(user,user.getRole(),getCredentialsById(user.getId()));
    }

    @Override
    public Credential getCredentialsById(Long id) {
        return credentialRepository.getCredentialByUserId(id)
                .orElseThrow(() -> new NotificationException("Credential Not Found")); //todo: excpetion
    }

    // todo: take look at the exception classes
    private final Function<String, Confirmation> getConfirmationByKey = key -> new Confirmation();/*confirmationRepository.findByKey(key)
            .orElseThrow(() -> new NotificationException("Confirmation Key Not Found"));*/

  /*  private final Function<String, User> getUserByEmail = email -> userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new NotificationException("User Not Found"));*/
    private User getUserByEmail(String email){
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotificationException("User Not Found"));
    }

    private User createNewUser(String firstName, String lastName, String email) {
        var role = getRole(Authority.USER.name());
        return createNewUserObj(firstName, lastName, email, role);
    }

}
