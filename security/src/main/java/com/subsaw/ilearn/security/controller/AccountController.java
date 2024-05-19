package com.subsaw.ilearn.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsaw.ilearn.security.common.JwtUtil;
import com.subsaw.ilearn.security.model.ChangePasswordDto;
import com.subsaw.ilearn.security.model.UsernamePasswordDto;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("account")
@RestController
@Slf4j
public class AccountController {
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("token")
    public String getToken(@RequestBody UsernamePasswordDto request){

        Authentication authentication = null;
        
        log.info("Generating JWT token for user {}", request.getUsername());
        try {
             authentication = authManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            log.info("JWT token generation for user {} failed {}", request.getUsername(), e.getMessage());
        }

        return jwtUtil.getToken(authentication);
    }

    @PostMapping("credentials")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok().build();
    }
}
