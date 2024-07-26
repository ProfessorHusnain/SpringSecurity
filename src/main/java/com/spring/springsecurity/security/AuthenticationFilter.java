package com.spring.springsecurity.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.springsecurity.domain.Response;
import com.spring.springsecurity.domain.Token;
import com.spring.springsecurity.dto.AuthenticatedUser;
import com.spring.springsecurity.dtorequest.LoginRequest;
import com.spring.springsecurity.service.JwtService;
import com.spring.springsecurity.service.UserService;
import com.spring.springsecurity.enumeration.LoginType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

import static com.spring.springsecurity.domain.ApplicationUsernamePasswordAuthenticationToken.unauthenticated;
import static com.spring.springsecurity.utils.RequestUtils.getResponse;
import static com.spring.springsecurity.utils.RequestUtils.handleErrorResponse;

// todo: filter  not working error no static resource found

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final UserService userService;
    private final JwtService jwtService;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/authenticate",
            "POST");



    protected AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        super(defaultFilterProcessesUrl, authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            var user = new ObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
                    .readValue(request.getInputStream(), LoginRequest.class);
            userService.updatedLoginAttempt(user.getUsername(), LoginType.LOGIN_ATTEMPT);
            var authentication = unauthenticated(user.getUsername(), user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        var user = (AuthenticatedUser) authentication.getPrincipal();
        userService.updatedLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);
        var httpResponse = user.isMfa() ?
                sendQRCode(request, user) :
                sendResponse(request, response, user);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }
    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, AuthenticatedUser user) {
        //jwtService.addCookies(response, user, TokenType.ACCESS);;
        //jwtService.addCookies(response, user, TokenType.REFRESH);
        Token tokens= Token.builder()
                .access(jwtService.generateToken(user, Token::getAccess))
                .refresh(jwtService.generateToken(user, Token::getRefresh))
                .build();

        return getResponse(request, Map.of("tokens",tokens),"Login successful",HttpStatus.OK);
    }
    private Response sendQRCode(HttpServletRequest request, AuthenticatedUser user) {

        return getResponse(request, Map.of("user",user),"Please Enter QR Code",HttpStatus.OK);
    }

}
