package com.subsaw.ilearn.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.subsaw.ilearn.security.common.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static String BEARER_TOKEN = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (authHeader == null) {
            filterChain.doFilter(request, response);
        }

        if(authHeader.isBlank() || authHeader.substring(BEARER_TOKEN.length()).isBlank() || !jwtUtil.validateToken(authHeader.substring(BEARER_TOKEN.length()))) {
            throw new AccessDeniedException("Invalid token");
        }
    }
    
}
