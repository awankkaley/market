package com.viaje.market.api_key;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;
import org.springframework.security.core.authority.AuthorityUtils;

@Transient
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private String apiKey;
    private String body;
    private String signature;


    public ApiKeyAuthenticationToken(String apiKey, String body, String signature, boolean authenticated) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        this.body = body;
        this.signature = signature;
        setAuthenticated(authenticated);
    }

    public ApiKeyAuthenticationToken(String apiKey, String body, String signature) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        this.body = body;
        this.signature = signature;
        setAuthenticated(false);
    }

    public ApiKeyAuthenticationToken() {
        super(AuthorityUtils.NO_AUTHORITIES);
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return signature;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    @Override
    public Object getDetails() {
        return body;
    }
}