package org.cris6h16.security;

import org.cris6h16.user.UserComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final UserComponent userComponent;

    SecurityConfig(JwtUtils jwtUtils, UserComponent userComponent) {
        this.jwtUtils = jwtUtils;
        this.userComponent = userComponent;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/carts",
                                "/api/v1/addresses").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/auth/refresh-token",
                                "/api/v1/carts/items/{itemId}/amount",
                                "/api/v1/carts/items",
                                "/api/v1/addresses").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/auth/login",
                                "/api/v1/auth/signup",
                                "/api/v1/emails/send-email-verification",
                                "/api/v1/users/verify-email",
                                "/api/v1/auth/reset-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/health").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/tests/reset-functional-testing-db",
                                "/api/v1/products").permitAll()
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/addresses/{id}").hasRole("USER")
                        .anyRequest().denyAll())
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, userComponent), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(conf -> conf.configurationSource(this.corsConfigurer()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .logout(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("SELLER")
                .role("SELLER").implies("USER")
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private CorsConfigurationSource corsConfigurer() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("*");
        cors.addAllowedMethod("*");
        cors.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlCors = new UrlBasedCorsConfigurationSource();
        urlCors.registerCorsConfiguration("/**", cors);

        return urlCors;
    }
}
