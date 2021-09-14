package com.lecture.security.security.config;

import com.lecture.security.security.common.FormAuthenticationDetailsSource;
import com.lecture.security.security.filter.AjaxLoginProcessingFilter;
import com.lecture.security.security.handler.CustomAccessDeniedHandler;
import com.lecture.security.security.provider.CustomAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private FormAuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    private UserDetailsService userDetailsService;

    //authenticationManagerBeam()과 아래 주석 authentciationManager의 차이?
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
/*    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }*/

    //db에서 사용자 정보 가져오는 것
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService);
        //
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider();
    }



    //db에서 사용자 정보 가져오는 것으로 변경.
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String password = passwordEncoder().encode("1111");

        auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
        auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER","USER");
        auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN","MANAGER","USER");
    }*/



    @Override
    public void configure(WebSecurity web) throws Exception {
        //static resource는 인증필터 타지 않도록 설정
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/users","user/login/**","/login","/api/login").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated()
        .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(authenticationDetailsSource) //username, password 외의 인증 파라미터.
                .defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler) //인증 성공 핸들러
                .failureHandler(customAuthenticationFailureHandler) //인증 실패 핸들러
                .permitAll() //이 경로에 해당하는 권한 통과하도록 설정한다.
        .and()
                //인가 예외가 발생할 경우 핸들러를 호출할 수 있도록 api를 제공한다. 이 api가 초기화 될 때 ExceptionTranslationFilter가 생성되고 작동하도록 초기화 된다.
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .accessDeniedPage("/denied");
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        AccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        ((CustomAccessDeniedHandler) accessDeniedHandler).setErrorPage("/denied");

        return accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}
