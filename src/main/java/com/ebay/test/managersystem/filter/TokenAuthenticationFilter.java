package com.ebay.test.managersystem.filter;

import com.ebay.test.managersystem.common.ReturnCode;
import com.ebay.test.managersystem.entity.User;
import com.ebay.test.managersystem.exception.AuthorizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, AuthorizationException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            resolver.resolveException(request, response, null, new AuthorizationException(ReturnCode.RC401.getCode(), "Missing authorizationHeader"));
            return;
        }
        String decodedToken = new String(Base64.getDecoder().decode(authorizationHeader));
        User user;
        try {
            user = objectMapper.readValue(decodedToken, User.class);
        } catch (UnrecognizedPropertyException e) {
            resolver.resolveException(request, response, null, new AuthorizationException(ReturnCode.RC401.getCode(), "Invalid authorizationHeader"));
            return;
        }
        if (user.getUserId() == null || user.getUserId() <= 0 || user.getAccountName() == null || user.getAccountName().isEmpty() || user.getRole() == null || user.getRole().isEmpty()) {
            resolver.resolveException(request, response, null, new AuthorizationException(ReturnCode.RC401.getCode(), "Invalid authorizationHeader"));
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getUserId(), null, Collections.singleton(new SimpleGrantedAuthority(user.getRole())));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
