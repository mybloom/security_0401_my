package com.lecture.security.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception)
            throws IOException, ServletException {
        String errMsg = "Invaild Username or Password";

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //예외 타입에 따라 다른 메세지를 던져준다.
        if(exception instanceof BadCredentialsException){
            errMsg =  "Invaild Username or Password";
        }else if(exception instanceof DisabledException){
            errMsg = "Locked";
        }else if(exception instanceof CredentialsExpiredException){
            errMsg = "Expired Password";
        }

        objectMapper.writeValue(httpServletResponse.getWriter(), errMsg);

    }
}
