package org.cris6h16;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService{

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


}
