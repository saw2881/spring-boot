package com.subsaw.ilearn.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JdbcAuthenticationManager implements AuthenticationManager {

    private static final Log logger = LogFactory.getLog(JdbcAuthenticationManager.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       logger.info(authentication.getPrincipal());
       logger.info(authentication.getCredentials());

       return UsernamePasswordAuthenticationToken.authenticated(authentication.getPrincipal(), authentication.getCredentials(), null);
    }
    
}
