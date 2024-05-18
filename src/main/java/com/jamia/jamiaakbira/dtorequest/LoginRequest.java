package com.jamia.jamiaakbira.dtorequest;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotEmpty(message = "Username cannot be empty or null")
    private String username;
    @NotEmpty(message = "Password cannot be empty or null")
    private String password;

}
