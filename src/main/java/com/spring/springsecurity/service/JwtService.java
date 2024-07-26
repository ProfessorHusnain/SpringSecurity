package com.spring.springsecurity.service;

import com.spring.springsecurity.domain.Token;
import com.spring.springsecurity.domain.TokenData;
import com.spring.springsecurity.dto.AuthenticatedUser;
import com.spring.springsecurity.enumeration.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.function.Function;

public interface JwtService {
    String generateToken(AuthenticatedUser user, Function<Token,String> tokenFunction);
    Optional<String> extractToken(HttpServletRequest request,String tokenType);
    Optional<String> extractCookie(HttpServletRequest request,String tokenType);

    void addCookies(HttpServletResponse response, AuthenticatedUser user, TokenType tokenType);
    <T> T getTokenData(String token,Function<TokenData,T> tokenFunction);

    void removeCookie(HttpServletRequest request,HttpServletResponse response,String cookieName);
}
