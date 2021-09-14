package com.lecture.security.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lecture.security.domain.dto.AccountDto;
import com.lecture.security.security.token.AjaxAuthenticationToken;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    //ObjectMapper: json으로 서버에 전달된 사용자 정보를 객체로 만드는 것
    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        //super(defaultFilterProcessesUrl);
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        if(!isAjax(httpServletRequest)){
            throw new IllegalStateException("*Authentication is not suppoted. only Ajax enable");    
        }

        //사용자가 입력한 id, password는 json방식으로 서버에 전달될텐데 해당 정보를 객체로 만든 후, 토큰으로 만들어 인증매니저에게 전달.
        AccountDto accountDto = objectMapper.readValue(httpServletRequest.getReader(), AccountDto.class);

        if(!StringUtils.hasLength(accountDto.getUsername()) || !StringUtils.hasLength(accountDto.getPassword())){
            throw new IllegalArgumentException("Username or Password is empty");
        }

        AjaxAuthenticationToken authenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(),accountDto.getPassword());

        //인증매니저에게 전달해서 인증을 처리하게 한다.
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private boolean isAjax(HttpServletRequest request) {
        //헤더의 정보를 활용해 ajax판단. 클라이언트와 협의를 통해 맞춘다.
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            return true;
        }

        return false;
    }
}
