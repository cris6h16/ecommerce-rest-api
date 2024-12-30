package org.cris6h16.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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


    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.debug("entered to JwtAuthenticationFilter.doFilterInternal");

        String requestURI = request.getRequestURI();

        // skip for `permitAll` endpoints
//        if (isPermitAllEndpoint(requestURI)) {
//            log.debug("PermitAll endpoint, skipping token validation: {}", requestURI);
//            filterChain.doFilter(request, response);
//            return;
//        }

        String token = getTokenFromRequest(request);

        if (isTokenInvalid(token, response)) {
            filterChain.doFilter(request, response);
            return;
        }

        Long id = jwtUtils.getId(token);
        Collection<? extends SimpleGrantedAuthority> authorities = jwtUtils.getAuthority(token).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserPrincipal user = new UserPrincipal(authorities, id);
        var authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Authenticated user: {}", user);

        filterChain.doFilter(request, response);
    }

    private boolean isTokenInvalid(String token, HttpServletResponse response) {
        if (token == null || !jwtUtils.validate(token)) {
            log.debug(token == null ? "Token is null, setting response status to 401" : "Token is invalid, setting response status to 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }
        return false;
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


//    private boolean isPermitAllEndpoint(String uri) {
//        return uri.matches("/api/v1/auth/(login|signup|verify-email|reset-password|send-email-verification)")
//                || uri.equals("/api/v1/products/categories")
//                || uri.equals("/api/v1/products");
//    }

}