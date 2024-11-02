package org.cris6h16;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
 class JwtUtilsImpl implements JwtUtils {

    private final JwtProperties jwtProperties;
    private final String ROLE_CLAIM = "roles";

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

    public Long getId(String token) {
        return Long.valueOf(getAClaim(token, Claims::getSubject));
    }

    public Set<String> getRoles(String token) {
        String rolesStr = getAClaim(token, claims -> claims.get(ROLE_CLAIM, String.class));

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
    public String genAccessToken(GenAccessTokenInput input) {
        log.debug("Generating refresh token for input: {}", input.toString());

        Map<String, String> claims = new HashMap<>();
        claims.put("enabled", String.valueOf(input.isEnabled()));
        claims.put("authorities", input.getAuthorities().toString());

        return genToken(
                input.getId(),
                claims,
                refreshAccessTokenExpSecs()
        );
    }

    private long refreshAccessTokenExpSecs() {
        long time = this.jwtProperties.getAccessTokenExp().getTime();
        String unit = this.jwtProperties.getAccessTokenExp().getUnit();
        return toSecs(time, unit);
    }

    @Override
    public String genRefreshToken(Long id) {
        log.debug("Generating access token for user ID: {}", id);
        return genToken(
                id,
                null,
                refreshRefreshTokenExpSecs()
        );
    }

    private long refreshRefreshTokenExpSecs() {
        long time = this.jwtProperties.getRefreshTokenExp().getTime();
        String unit = this.jwtProperties.getRefreshTokenExp().getUnit();
        return toSecs(time, unit);
    }

    private long toSecs(long time, String unit) {
        return switch (unit) {
            case "MINUTES" -> time * 60;
            case "DAYS" ->  time * 60 * 60 * 24;
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
    }


}