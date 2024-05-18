package com.jamia.jamiaakbira.domain;

import com.jamia.jamiaakbira.dto.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@Builder
public class TokenData {
    private AuthenticatedUser user;
    private Claims claims;
    private boolean valid;
    private List<GrantedAuthority> authorities;
}
