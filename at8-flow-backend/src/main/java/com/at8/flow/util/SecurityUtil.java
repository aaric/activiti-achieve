package com.at8.flow.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * SecurityUtil
 *
 * @author Aaric
 * @version 0.10.0-SNAPSHOT
 */
@Slf4j
@Component
public class SecurityUtil {

    @Autowired
    @Qualifier("inMemoryUserDetailsService")
    private UserDetailsService userDetailsService;

    public void loginAs(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (null != userDetails) {
            SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return userDetails.getAuthorities();
                }

                @Override
                public Object getCredentials() {
                    return userDetails.getPassword();
                }

                @Override
                public Object getDetails() {
                    return userDetails;
                }

                @Override
                public Object getPrincipal() {
                    return userDetails;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }

                @Override
                public String getName() {
                    return userDetails.getUsername();
                }
            }));
        }
    }
}
