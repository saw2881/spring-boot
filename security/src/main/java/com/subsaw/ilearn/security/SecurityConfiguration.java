package com.subsaw.ilearn.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.subsaw.ilearn.security.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserRepository userRepository;

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

    @Bean
    public UserDetailsService getUserDetailsService(){

        return username ->  {
            var usersList = userRepository.findByUsername(username);

            if(usersList.size() == 0)
                return null;

            else {
                var user = usersList.get(0);
                return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(role -> 
                        new SimpleGrantedAuthority(role.getRole()))
                        .collect(Collectors.toList()))
                .build();
            }
         };
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "com.subsaw.ilearn.db.seed", havingValue = "true")
    public CommandLineRunner seedData(PasswordEncoder passwordEncoder) {
        return args -> {
            // if there is data, don't bother
            if (userRepository.count() > 0) 
                return;
            
            var user = com.subsaw.ilearn.security.entity.User.builder()
            .firstName("John")
            .lastName("Doe")
            .username("john.doe")
            .password(passwordEncoder.encode("p@$$w0rd"))
            .build();

            userRepository.save(user);
        } ;
    }
}
