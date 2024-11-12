package org.cris6h16.security;

import java.util.Collection;
import java.util.Set;

interface JwtUtils {
    String genAccessToken(Long id, Set<String> authorities);

    String genRefreshToken(Long id, Set<String> authorities);

    boolean validate(String token);

    Long getId(String token);

    Collection<String> getAuthority(String token);
}