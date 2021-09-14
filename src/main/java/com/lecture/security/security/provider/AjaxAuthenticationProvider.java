package com.lecture.security.security.provider;

import com.lecture.security.security.common.FormWebAuthenticationDetails;
import com.lecture.security.security.service.AccountContext;
import com.lecture.security.security.token.AjaxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AjaxAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //인증에 대한 검증을 위한 구현
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String)authentication.getCredentials(); //패스워드가 참조됨

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        //사용자가 입력한 password, DB에 저장된 암호화된 정보가 같은지 확인한다.
        if(!passwordEncoder.matches(password, accountContext.getPassword())){
            throw new BadCredentialsException("*BadCredentialsException!");
        }

        //아이디, 패스워드 이외의 파라미터 검증도 추가적으로 할 수 있다. 4-8)WebAuthenticationDetails
        /*FormWebAuthenticationDetails formWebAuthenticationDetails =  (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = formWebAuthenticationDetails.getSecretKey();
        if(secretKey == null || !"secret".equals(secretKey)){
            throw new InsufficientAuthenticationException("*InsufficientAuthenticationException!");
        }*/


        //password검증에 성공하면 아래 token을 만든다.
        //인증정보를 담은 객체를 만들어서 provider 호출한 AuthenticationManager에게 이 정보를 리턴하도록 한다.
        AjaxAuthenticationToken authenticationToken
                = new AjaxAuthenticationToken(accountContext,null, accountContext.getAuthorities());


        return authenticationToken;
    }

    //파라미터로 전달되는 authenticiation타입과 == CustomAuthenticationProvider가 사용하고자 하는 토큰의 타입과 일치할 때 인증처리 할 수 있도록 한다.
    @Override
    public boolean supports(Class<?> authentication) {
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
