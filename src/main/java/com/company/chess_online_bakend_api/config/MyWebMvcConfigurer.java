package com.company.chess_online_bakend_api.config;

import com.company.chess_online_bakend_api.config.handler.MySavedRequestAwareAuthenticationSuccessHandler;
import com.company.chess_online_bakend_api.config.handler.MySimpleUrlAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class MyWebMvcConfigurer extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccesHandler;
    private final MySimpleUrlAuthenticationFailureHandler myFailureHandler;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MyWebMvcConfigurer(MyUserDetailsService myUserDetailsService, RestAuthenticationEntryPoint
            restAuthenticationEntryPoint, MySavedRequestAwareAuthenticationSuccessHandler mySuccesHandler,
                              MySimpleUrlAuthenticationFailureHandler myFailureHandler,
                              BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.myUserDetailsService = myUserDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.mySuccesHandler = mySuccesHandler;
        this.myFailureHandler = myFailureHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .formLogin().permitAll()
                .successHandler(mySuccesHandler)
                .failureHandler(myFailureHandler)
                .loginProcessingUrl("/authentication/login")
                .and()
                .logout().logoutUrl("/authentication/logout").permitAll()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
