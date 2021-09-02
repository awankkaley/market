package com.viaje.market.api_key;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import sun.security.util.IOUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

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
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object body = new ObjectMapper()
                .readValue(request.getInputStream(), Object.class);
        String bodyJson = new Gson().toJson(body);
        String apiKeyOptional = request.getHeader(apiKeyConfiguration.getPrincipalRequestHeader());
        String tokenHeader = request.getHeader(apiKeyConfiguration.getSignRequestHeader());
        ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(apiKeyOptional, bodyJson, tokenHeader);
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