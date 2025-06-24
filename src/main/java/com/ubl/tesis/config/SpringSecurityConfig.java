package com.ubl.tesis.config;


import com.ubl.tesis.util.Constant;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 *
 * @author RR
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Value("${api.key}")
    private String apiKey;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**",
            "/ping"
    };

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(AUTH_WHITELIST).permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager())
                .addFilterAt(apiKeyAuthFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            if (authentication != null && authentication.getCredentials() != null) {
                return Mono.just(authentication);
            } else {
                return Mono.error(new BadCredentialsException("UNAUTHORIZED"));
            }
        };
    }

    @Bean
    public AuthenticationWebFilter apiKeyAuthFilter() {
        AuthenticationWebFilter apiKeyAuthFilter = new AuthenticationWebFilter(authenticationManager());
        apiKeyAuthFilter.setServerAuthenticationConverter(exchange -> {
            String receivedApiKey = exchange.getRequest().getHeaders().getFirst(Constant.X_API_KEY);
            if (apiKey.equals(receivedApiKey)) {
                return Mono.just(new UsernamePasswordAuthenticationToken(
                        null,
                        receivedApiKey,
                        Collections.singletonList(new SimpleGrantedAuthority("REGISTERED"))));
            }
            return Mono.empty();
        });
        return apiKeyAuthFilter;
    }
}
