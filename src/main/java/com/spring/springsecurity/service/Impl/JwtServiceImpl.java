package com.spring.springsecurity.service.Impl;

import com.spring.springsecurity.domain.Token;
import com.spring.springsecurity.domain.TokenData;
import com.spring.springsecurity.dto.AuthenticatedUser;
import com.spring.springsecurity.enumeration.TokenType;
import com.spring.springsecurity.function.TriConsumer;
import com.spring.springsecurity.security.JwtConfiguration;
import com.spring.springsecurity.service.JwtService;
import com.spring.springsecurity.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.spring.springsecurity.constant.Constant.*;
import static java.time.Instant.now;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static org.apache.tomcat.util.http.SameSiteCookies.NONE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl extends JwtConfiguration implements JwtService {

    private final UserService userService;

    private final Supplier<SecretKey> key = () -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(getSecret()));
    private final Function<String, Claims> parseClaims = token -> Jwts.parser()
            .verifyWith(key.get())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    private final Function<String, String> subject = token -> getClaimsValue(token, Claims::getSubject);

    private <T> T getClaimsValue(String token, Function<Claims, T> claims) {
        return parseClaims.andThen(claims).apply(token);
    }

    private final BiFunction<HttpServletRequest, String, Optional<String>> extractToken =
            (request, cookieName) -> {
                String header = request.getHeader(AUTHORIZATION);
                if (header == null || !header.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
                    return Optional.empty();
                }

                return Optional.of(header.substring(AUTHORIZATION_HEADER_PREFIX.length() + 1));
            };
    private final BiFunction<HttpServletRequest, String, Optional<Cookie>> extractCookie =
            (request, cookieName) ->
                    Optional.of(
                            stream(
                                    request.getCookies() == null ?
                                            new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} :
                                            request.getCookies()
                            ).filter(cookie -> Objects.equals(cookie.getName(), cookieName))
                                    .findAny()
                    ).orElse(empty());
    private final BiFunction<HttpServletRequest, String, Optional<String>> extractCookieValue =
            (request, cookieName) ->
                    Optional.of(
                            stream(
                                    request.getCookies() == null ?
                                            new Cookie[]{new Cookie(EMPTY_VALUE, EMPTY_VALUE)} :
                                            request.getCookies()
                            ).filter(cookie -> Objects.equals(cookie.getName(), cookieName))
                                    .map(Cookie::getValue)
                                    .findAny()
                    ).orElse(empty());
    private final Supplier<JwtBuilder> builder =
            () ->
                    Jwts.builder()
                            .header().add(Map.of(TYPE, JWT_TYPE))
                            .and()
                            .audience().add(APPLICATION_LLC)
                            .and()
                            .id(UUID.randomUUID().toString())
                            .issuedAt(Date.from(now()))
                            .notBefore(Date.from(now()))
                            .signWith(key.get(), Jwts.SIG.HS512);

    private final BiFunction<AuthenticatedUser, TokenType, String> buildToken =
            (user, tokenType) ->
                    Objects.equals(tokenType, TokenType.ACCESS) ?
                            builder.get()
                                    .subject(user.getUserId())
                                    .claim(AUTHORITIES, user.getAuthorities())
                                    .claim(ROLE, user.getRole())
                                    .expiration(Date.from(now().plusSeconds(getExpiration())))
                                    .compact() :
                            builder.get()
                                    .subject(user.getUserId())
                                    .expiration(Date.from(now().plusSeconds(getExpiration() + getExpiration())))
                                    .compact();
    private final Function<String, Boolean> isTokenValid = token ->
            now().isBefore(parseClaims.apply(token).getExpiration().toInstant()); /*&&
            Objects.equals(
                    userService.getAuthenticatedUserByUserId(subject.apply(token)).getUserId(), parseClaims.apply(token).getSubject()
            );*/

    private final TriConsumer<HttpServletResponse, AuthenticatedUser, TokenType> addCookie =
            (response, user, type) ->
            {
                switch (type) {
                    case ACCESS -> {
                        var accessToken = generateToken(user, Token::getAccess);
                        var cookie = new Cookie(type.getValue(), accessToken);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(true); //todo: un commit in production
                        cookie.setMaxAge(2 * 60 * 60);
                        cookie.setPath("/");
                        cookie.setAttribute("SameSite", NONE.name());
                        response.addCookie(cookie);
                    }
                    case REFRESH -> {
                        var refreshToken = generateToken(user, Token::getRefresh);
                        var cookie = new Cookie(type.getValue(), refreshToken);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(true); //todo: un commit in production
                        cookie.setMaxAge(2 * 60 * 60);
                        cookie.setPath("/");
                        cookie.setAttribute("SameSite", NONE.name());
                        response.addCookie(cookie);
                    }
                }
            };

    public Function<String, List<GrantedAuthority>> authorities =
            token ->
                    commaSeparatedStringToAuthorityList(new StringJoiner(AUTHORITY_DELIMITER)
                            .add(parseClaims.apply(token).get(AUTHORITIES, String.class))
                            .add(ROLE_PREFIX + parseClaims.apply(token).get(ROLE, String.class)).toString()
                    );

    @Override
    public String generateToken(AuthenticatedUser user, Function<Token, String> tokenFunction) {
        return tokenFunction.apply(Token.builder()
                .access(buildToken.apply(user, TokenType.ACCESS))
                .refresh(buildToken.apply(user, TokenType.REFRESH))
                .build());
    }

    @Override
    public Optional<String> extractToken(HttpServletRequest request, String tokenType) {
        return extractToken.apply(request, tokenType);
    }

    @Override
    public Optional<String> extractCookie(HttpServletRequest request, String tokenType) {
        return extractCookieValue.apply(request, tokenType);
    }

    @Override
    public void addCookies(HttpServletResponse response, AuthenticatedUser user, TokenType tokenType) {
        addCookie.accept(response, user, tokenType);
    }

    @Override
    public <T> T getTokenData(String token, Function<TokenData, T> tokenFunction) {
        return tokenFunction.apply(
                TokenData.builder()
                        .valid(isTokenValid.apply(token))
                        //.authorities(authorities.apply(token))
                        .claims(parseClaims.apply(token))
                        .user(userService.getAuthenticatedUserByUserId(subject.apply(token)))
                        .build()
        );
    }

    @Override
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        extractCookie.apply(request, cookieName).ifPresent(cookie -> {
            cookie.setMaxAge(0);
            cookie.setValue(EMPTY_VALUE);
            cookie.setPath("/");
            response.addCookie(cookie);
        });
    }
}
