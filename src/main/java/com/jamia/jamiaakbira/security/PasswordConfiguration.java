package com.jamia.jamiaakbira.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.jamia.jamiaakbira.constant.Constant.PASSWORD_STRENGTH;

@Configuration
public class PasswordConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_STRENGTH);
    }

}
