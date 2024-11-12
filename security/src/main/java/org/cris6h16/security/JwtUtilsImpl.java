package org.cris6h16.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
class JwtUtilsImpl implements JwtUtils {

    private final JwtProperties jwtProperties;
    private final String AUTHORITY_CLAIM = "authorities";

    public JwtUtilsImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    String genToken(Long subject, Map<String, String> claims, long timeExpirationSecs) {
        log.debug("Generating token");

        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(String.valueOf(subject))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (timeExpirationSecs * 1000)))
                .signWith(getSign());

        if (claims != null && !claims.isEmpty()) {
            for (Map.Entry<String, String> entry : claims.entrySet()) {
                jwtBuilder.claim(entry.getKey(), entry.getValue());

                log.debug("Added claim: {} = {}", entry.getKey(), entry.getValue());
            }
        }

        String tk = jwtBuilder.compact();

        log.debug("Token generated");
        log.trace("Token generated: {}", tk);
        return tk;
    }

    @Override
    public boolean validate(String token) {
        log.debug("Validating token");
        try {
            Jwts.parser()
                    .verifyWith(getSign())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("Token is valid");
            return true;
        } catch (Exception e) {
            log.debug("Token is invalid");
            return false;
        }
    }

    @Override
    public Long getId(String token) {
        return Long.valueOf(getAClaim(token, Claims::getSubject));
    }

    @Override
    public Set<String> getAuthority(String token) {
        String rolesStr = getAClaim(token, claims -> claims.get(AUTHORITY_CLAIM, String.class));

        if (rolesStr == null || rolesStr.isEmpty()) {
            log.debug("No roles claim found in token");
            return Collections.emptySet();
        }

        return Arrays.stream(rolesStr
                        .replaceAll("[\\[\\]\\s]", "")
                        .split(","))
                .collect(Collectors.toSet());
    }

    <T> T getAClaim(String token, Function<Claims, T> individualClaim) {
        log.debug("Extracting a claim from token, function: {}", individualClaim.toString());
        Claims claims = getClaims(token);
        return individualClaim.apply(claims);
    }


    Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSign())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    SecretKey getSign() {
        byte[] keyBase = Decoders.BASE64.decode(this.jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBase);
    }

    @Override
    public String genAccessToken(Long id, Set<String> authorities) {
        long mins = this.jwtProperties.getAccessTokenExpMinutes();
        Map<String, String> claims = new HashMap<>();
        claims.put("authorities", authorities.toString());

        log.debug("Generating access token for user ID: {}, authorities: {}, expiration time: {} minutes", id, authorities, mins);
        return genToken(id, claims, 60 * mins);
    }

    @Override
    public String genRefreshToken(Long id, Set<String> authorities) {
        long mins = this.jwtProperties.getRefreshTokenExpMinutes();
        Map<String, String> claims = new HashMap<>();
        claims.put("authorities", authorities.toString());

        log.debug("Generating refresh token for user ID: {}, authorities: {}, expiration time: {} minutes", id, authorities, mins);
        return genToken(id, claims, 60 * mins);
    }


}