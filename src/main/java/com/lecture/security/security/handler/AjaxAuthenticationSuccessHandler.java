package com.lecture.security.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lecture.security.domain.entitiy.Account;
import com.lecture.security.security.service.AccountContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException, ServletException {
        //Account account = (Account) authentication.getPrincipal();
        AccountContext accountContext = ((AccountContext) authentication.getPrincipal());

        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //objectMapper가 json형식으로 변환해서 클라이언트에 반환해준다.
        objectMapper.writeValue(httpServletResponse.getWriter(), accountContext);
    }


}
