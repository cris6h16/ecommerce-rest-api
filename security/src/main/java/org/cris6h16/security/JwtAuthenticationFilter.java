package org.cris6h16.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.UserComponent;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserComponent userComponent;


    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserComponent userComponent) {
        this.jwtUtils = jwtUtils;
        this.userComponent = userComponent;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.debug("entered to JwtAuthenticationFilter.doFilterInternal");

        String token = getTokenFromRequest(request);

        if (token == null || !jwtUtils.validate(token)) {
            log.debug("Token is null or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        Long userId = jwtUtils.getId(token);
        Collection<? extends SimpleGrantedAuthority> authorities = jwtUtils.getAuthority(token).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        if (!isEnabledUser(userId)) {
            log.debug("User with id: {} is not enabled", userId);
            filterChain.doFilter(request, response);
            return;
        }

        UserPrincipal user = new UserPrincipal(authorities, userId);
        var authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Authenticated user: {}", user);

        filterChain.doFilter(request, response);
    }

    private boolean isEnabledUser(Long userId) {
        return userComponent.existsByIdAndEnabled(userId, true);
    }


    protected String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer == null) {
            log.debug("Authorization header are null");
            return null;
        }

        return extractToken(bearer);
    }

    private String extractToken(String bearer) {
        log.debug("Extracting token from: '{}'", bearer);
        String token = bearer.substring(7); // *beginIndex* <|> 'Bearer eyJh....'
        log.debug("Extracted token: '{}'", token);
        return token;
    }



}