package org.cris6h16.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityComponent {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public SecurityServiceImpl(PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
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
    public String generateAccessToken(GenAccessTokenInput input) {
        return jwtUtils.genAccessToken(input);
    }

    @Override
    public String generateRefreshToken(Long id) {
        return jwtUtils.genRefreshToken(id);
    }

    @Override
    public Long getCurrentUserId() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof UserPrincipal userPrincipal) {
            return ((UserPrincipal) obj).getId();
        }
        throw new IllegalStateException("Principal is not an instance of UserPrincipal");
    }


}
