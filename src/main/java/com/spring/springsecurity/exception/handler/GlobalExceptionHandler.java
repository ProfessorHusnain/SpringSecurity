package com.spring.springsecurity.exception.handler;

import com.spring.springsecurity.domain.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.time.LocalTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleDevoteeNotFoundException(Exception ex, HttpServletRequest request) {
    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                    new Response(now().toString(),
                            HttpStatus.FORBIDDEN.value(),
                            request.getRequestURI(),
                            HttpStatus.FORBIDDEN,ex.getMessage(),
                            getRootCauseMessage(ex),emptyMap()));
    }
}
