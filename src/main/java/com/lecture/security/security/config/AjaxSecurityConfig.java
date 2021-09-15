package com.lecture.security.security.config;

import com.lecture.security.security.common.AjaxAcessDeniedHandler;
import com.lecture.security.security.common.AjaxLoginAuthenticationEntryPoint;
import com.lecture.security.security.filter.AjaxLoginProcessingFilter;
import com.lecture.security.security.handler.AjaxAuthenticationFailureHandler;
import com.lecture.security.security.handler.AjaxAuthenticationSuccessHandler;
import com.lecture.security.security.provider.AjaxAuthenticationProvider;
import com.lecture.security.security.token.AjaxAuthenticationToken;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")//모든 url정보에 대해서 받는게 아니라 특정한 url정보만 해당 필터 작동하도록 설정
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MANAGER")
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated();
//        .and()
//                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .exceptionHandling()
                        .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                                .accessDeniedHandler(ajaxAccessDeniedHandler());


       // http.csrf().disable();

        //AjaxLoginConfigurer가 적용되게 설정한다.
        customConfigurerAjax(http);

    }

    private void customConfigurerAjax(HttpSecurity http) throws Exception {
        http
                .apply(new AjaxLoginConfigurer<>())
                .successHandlerAjax(ajaxAuthenticationSuccessHandler())
                .failureHandlerAjax(ajaxAuthenticationFailureHandler())
                .setAuthenticationManager(authenticationManagerBean())
                .loginProcessingUrl("/api/login");
    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler(){
        return new AjaxAcessDeniedHandler();
    }

    @Bean
    public AjaxAuthenticationProvider ajaxAuthticationProvider(){
        return new AjaxAuthenticationProvider();
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler(){
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler(){
        return  new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //AjaxLoginController생성 이후 주석처리
//    @Bean
//    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
//        AjaxLoginProcessingFilter ajaxLoginProcessingFilter =  new AjaxLoginProcessingFilter();
//
//        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
//        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
//        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
//
//
//        return ajaxLoginProcessingFilter;
//    }



}
