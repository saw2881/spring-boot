package com.subsaw.ilearn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(requestMatcher -> {
            requestMatcher.requestMatchers(HttpMethod.POST,"/account/token").permitAll();
           requestMatcher.requestMatchers("/public/**").permitAll();
           requestMatcher.requestMatchers("**").authenticated();
        })
        .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
        .httpBasic(t-> {})
        .cors(t -> t.disable())
        .csrf(t -> t.disable())
        .build();
    }

    // @Bean
    // @Order(1)
    // public SecurityFilterChain jdbcFilterChain(HttpSecurity httpSecurity) throws Exception {
    //     return httpSecurity.authorizeHttpRequests(requestMatcher -> {
    //        requestMatcher.requestMatchers("/jdbc/**")
    //        .authenticated();
    //     }).authenticationManager(new JdbcAuthenticationManager())
    //     // .httpBasic(t ->  {})
    //     .build();
    // }

    @Bean
    public UserDetailsService getUserDetailsService(){
         return username -> User.builder()
         .username(username)
         .password(username)
         .authorities("bar")
         .build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
