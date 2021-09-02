package com.viaje.market.api_key;

import com.viaje.market.HmacValidator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
@AllArgsConstructor
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyConfiguration apiKeyConfiguration;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getPrincipal();
        String signature = (String) authentication.getCredentials();
        String body = (String) authentication.getDetails();
        log.error("---KEY---" + apiKey);
        log.error("---SIGNATURE---" + signature);
        log.error("---BODY---" + body);
        if (ObjectUtils.isEmpty(apiKey)) {
            throw new InsufficientAuthenticationException("No API key in request");
        } else {
            if (!apiKeyConfiguration.getPrincipalRequestValue().equals(apiKey)) {
                throw new BadCredentialsException("API Key is invalid");
            }
            if (HmacValidator.HashIsValid(apiKeyConfiguration.getSecretKey(), body, signature)) {
                return new ApiKeyAuthenticationToken(apiKey, body, signature, true);

            }
            throw new BadCredentialsException("API Key is invalid");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}