package com.jamia.jamiaakbira.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamia.jamiaakbira.domain.Response;
import com.jamia.jamiaakbira.exception.NotificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import javax.security.auth.login.CredentialExpiredException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.jamia.jamiaakbira.constant.Constant.EMPTY;
import static java.time.LocalTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse,Response> writeResponse=(httpServeltResponse,response)->{
       try {
           var outputStream=httpServeltResponse.getOutputStream();
           new ObjectMapper().writeValue(outputStream,response);
              outputStream.flush();
       }catch (Exception e){
           throw new NotificationException(e.getMessage());
       }
    };
    private static final BiFunction<Exception,HttpStatus,String> errorReason=(exception,status)->{
        if (status.isSameCodeAs(HttpStatus.FORBIDDEN)){
            return "You don't have enough permission to access this resource";
        }
        if (status.isSameCodeAs(HttpStatus.UNAUTHORIZED)){
            return "You need to login to access this resource";
        }
        // todo: add the custom exception class
        if (exception instanceof DisabledException || exception instanceof LockedException || exception instanceof BadCredentialsException || exception instanceof CredentialExpiredException || exception instanceof NotificationException){
            return exception.getMessage();
        }
        if (status.is5xxServerError()){
            return "An internal server error occurred";
        }else {
            return "An error occurred. Please trying again later";
        }
    };
    public static Response getResponse(HttpServletRequest request, Map<?,?> data, String message, HttpStatus status){
        return new Response(now().toString(),status.value(),request.getRequestURI(),status,message, EMPTY,data);
    }
    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response,Exception exception){
        if (exception instanceof AccessDeniedException){
            Response apiResponse=getErrorResponse(request,response,exception, HttpStatus.FORBIDDEN);
            writeResponse.accept(response,apiResponse);
        }else {
            Response apiResponse=getErrorResponse(request,response,exception, HttpStatus.BAD_REQUEST);
            writeResponse.accept(response,apiResponse);
        }
    }

    public static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus status){
        response.setContentType("application/json");
        response.setStatus(status.value());
        return new Response(now().toString(),status.value(),request.getRequestURI(),HttpStatus.valueOf(status.value()),errorReason.apply(exception,status), getRootCauseMessage(exception),emptyMap());
    }

}
