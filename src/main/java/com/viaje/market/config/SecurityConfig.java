package com.viaje.market.config;


import com.viaje.market.api_key.ApiKeyAuthenticationFilter;
import com.viaje.market.api_key.ApiKeyAuthenticationProvider;
import com.viaje.market.api_key.ApiKeyConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ApiKeyConfiguration apiKeyConfiguration;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new ApiKeyAuthenticationFilter(authenticationManager(), apiKeyConfiguration), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
//                .antMatchers(
//                        "/api/v1/**"
//                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationExceptionEntryPoint(handlerExceptionResolver));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new ApiKeyAuthenticationProvider(apiKeyConfiguration));
    }
}
