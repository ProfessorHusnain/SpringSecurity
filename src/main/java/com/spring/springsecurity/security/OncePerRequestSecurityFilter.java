package com.spring.springsecurity.security;

import com.spring.springsecurity.domain.ApplicationUsernamePasswordAuthenticationToken;
import com.spring.springsecurity.domain.RequestContext;
import com.spring.springsecurity.domain.TokenData;
import com.spring.springsecurity.enumeration.TokenType;
import com.spring.springsecurity.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OncePerRequestSecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<String> accessToken = jwtService.extractToken(request, TokenType.ACCESS.getValue());
        if (accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        TokenData data = jwtService.getTokenData(accessToken.get(), tokenData -> tokenData);
        if (!data.isValid()) {
            jwtService.removeCookie(request, response, TokenType.ACCESS.getValue());
            filterChain.doFilter(request, response);
            return;
        }
        ApplicationUsernamePasswordAuthenticationToken authentication
                = new ApplicationUsernamePasswordAuthenticationToken(data.getUser(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(data.getUser().getAuthorities().toString()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        RequestContext.setUserId(data.getUser().getId());
        filterChain.doFilter(request, response);
    }
}
