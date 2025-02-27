package com.itelectric.backend.v1.config;

import com.itelectric.backend.v1.domain.enums.Roles;
import com.itelectric.backend.v1.utils.FuncUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${keycloak.client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/companies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**")
                        .hasRole(FuncUtils.getRemoveRolePrefix(Roles.ROLE_ADMIN.name()))

                        .requestMatchers("/api/v1/quotations/**")
                        .hasAnyRole(FuncUtils.getRemoveRolePrefix(Roles.ROLE_CUSTOMER.name())
                                , FuncUtils.getRemoveRolePrefix(Roles.ROLE_COMPANY.name()))

                        .requestMatchers(HttpMethod.GET, "/api/v1/services/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/services/**")
                        .hasRole(FuncUtils.getRemoveRolePrefix(Roles.ROLE_ADMIN.name()))

                        .requestMatchers(HttpMethod.POST, "/api/v1/admins/**")
                        .hasRole(FuncUtils.getRemoveRolePrefix(Roles.ROLE_SUPERUSER.name()))

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
            authorities.addAll(extractRoles(jwt));
            return authorities;
        });

        return jwtAuthenticationConverter;
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return List.of();
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(this.clientId);
        if (clientAccess == null || !clientAccess.containsKey("roles")) return List.of();
        List<String> roles = (List<String>) clientAccess.get("roles");
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
