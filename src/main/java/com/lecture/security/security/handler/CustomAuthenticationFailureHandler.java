package com.lecture.security.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //인증 검증 할 때 실패하면 인증 예외를 발생하는데
        //보통은 AuthenticationProvider, UserDetailsService에서 발생한다.

        String errorMessage = "Invaild Username or Password";

        //예외 타입에 따라 다른 메세지를 던져준다.
        if(exception instanceof BadCredentialsException){
            errorMessage =  "Invaild Username or Password";
        }else if(exception instanceof InsufficientAuthenticationException){
            errorMessage = "Invaild Secret Key";
        }

        //예외를 loginpage로 이동하게 하기 때문에 LoginController에서 처리하게 한다.
        //setDefaultFailureUrl("/login?error=true&exception=" + exception.getMessage());
        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);

        //response에서 응답을 해야 한다. 위임처리.
        super.onAuthenticationFailure(request,response,exception);

    }
}
