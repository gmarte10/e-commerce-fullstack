package com.giancarlos.config;

import com.giancarlos.service.JwtService;
import com.giancarlos.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtFilter is a custom filter that intercepts HTTP requests to handle JWT authentication.
 * It extends `OncePerRequestFilter` to ensure the filter is applied once per request.
 * This filter checks for the presence of a JWT token in the `Authorization` header of incoming requests
 * and validates it using the `JwtService`. If the token is valid, it sets the authentication in the
 * Spring Security context, allowing the request to proceed with the authenticated user.
 *
 * The filter performs the following steps:
 * 1. Extracts the JWT token from the `Authorization` header if it starts with "Bearer ".
 * 2. Extracts the username from the token.
 * 3. If the username is valid and no authentication is set, it loads user details from the `UserDetailService`.
 * 4. Validates the JWT token using the `JwtService`.
 * 5. If the token is valid, sets the authentication in the security context using `UsernamePasswordAuthenticationToken`.
 * 6. Passes the request along the filter chain.
 *
 * @component
 * @see JwtService
 * @see UserDetailService
 * @see UsernamePasswordAuthenticationToken
 * @see OncePerRequestFilter
 */

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = applicationContext.getBean(UserDetailService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken uPassAuthToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
