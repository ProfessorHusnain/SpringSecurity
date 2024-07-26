package com.spring.springsecurity.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class JwtConfiguration {
    @Value("${security.jwt.expiration}")
    private Long expiration;
    @Value("${security.jwt.secret}")
    private String secret;
}
