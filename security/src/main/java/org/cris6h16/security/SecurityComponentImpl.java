package org.cris6h16.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SecurityComponentImpl implements SecurityComponent {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    SecurityComponentImpl(PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String generateAccessToken(Long id, String authority) {
        return jwtUtils.genAccessToken(id, Collections.singleton(authority));
    }

    @Override
    public String generateRefreshToken(Long id, String authority) {
        return jwtUtils.genRefreshToken(id, Collections.singleton(authority));
    }

    @Override
    public Long getCurrentUserId() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof UserPrincipal userPrincipal) {
            return ((UserPrincipal) obj).getId();
        }
        throw new IllegalStateException("Principal is not an instance of UserPrincipal");
    }

    @Override
    public String getCurrentUserAuthority() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof UserPrincipal userPrincipal) {
            Set<String> authorities = ((UserPrincipal) obj).getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
            if (authorities.size() != 1) {
                log.error("User should have exactly one authority, but has {}", authorities);
            }
            return authorities.iterator().next();
        }
        throw new IllegalStateException("Principal is not an instance of UserPrincipal");
    }

    @Override
    public boolean isTokenValid(String token) {
        return jwtUtils.validate(token);
    }


}
