package com.viaje.market.api_key;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


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

        Optional<String> apiKeyOptional = Optional.ofNullable(request.getHeader(apiKeyConfiguration.getPrincipalRequestHeader()));

        ApiKeyAuthenticationToken token =
                apiKeyOptional.map(ApiKeyAuthenticationToken::new).orElse(new ApiKeyAuthenticationToken());

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