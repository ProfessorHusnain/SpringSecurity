package com.jamia.jamiaakbira.security;

import com.jamia.jamiaakbira.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        ApplicationAuthenticationProvider provider =
                new ApplicationAuthenticationProvider(userService, passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin",
                "Content-Type", "Accept", "JwtToken", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Access-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin",
                "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }


}
