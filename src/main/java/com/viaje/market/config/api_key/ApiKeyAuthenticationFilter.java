package com.viaje.market.config.api_key;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j

public class ApiKeyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ApiKeyConfiguration apiKeyConfiguration;

    public ApiKeyAuthenticationFilter(AuthenticationManager authenticationManager, ApiKeyConfiguration apiKeyConfiguration) {
        super("/api/v1/**");
        this.setAuthenticationManager(authenticationManager);
        this.apiKeyConfiguration = apiKeyConfiguration;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {
        String apiKeyOptional = request.getHeader(apiKeyConfiguration.getPrincipalRequestHeader());
        ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(apiKeyOptional);
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}