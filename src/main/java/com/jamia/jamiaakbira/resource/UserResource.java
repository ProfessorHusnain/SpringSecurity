package com.jamia.jamiaakbira.resource;

import com.jamia.jamiaakbira.domain.Response;
import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import com.jamia.jamiaakbira.dtorequest.UserRequest;
import com.jamia.jamiaakbira.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.jamia.jamiaakbira.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(getUri())
                .body(getResponse(request, emptyMap(), "Account created. Check your email for enable your account.", CREATED));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('profile:view')")
    public ResponseEntity<Response> getUserProfile(HttpServletRequest request) {
        AuthenticatedUser authenticatedUser = userService.authenticatedUserMetaData();
        return ResponseEntity.ok()
                .body(getResponse(request, Map.of("user",authenticatedUser), "User profile retrieved", OK));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyUserAccount(@RequestParam("key") String key, HttpServletRequest request) {
        userService.verifyAccountKey(key);
        return ResponseEntity.ok()
                .body(getResponse(request, emptyMap(), "Verification has been completed", OK));
    }

    private URI getUri() {
        return URI.create("");
    }
}
