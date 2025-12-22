package com.product.api.gitmarket.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //Liberadas
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/cadastro").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        //Clientes
                        .requestMatchers(HttpMethod.GET, "/api/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**").hasAnyRole("ADMIN", "USER")
                        //Produtos
                        .requestMatchers(HttpMethod.POST, "/api/produtos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/produtos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/produtos/**").hasRole("ADMIN")
                        //Categorias
                        .requestMatchers(HttpMethod.POST, "/api/categorias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/categorias/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}