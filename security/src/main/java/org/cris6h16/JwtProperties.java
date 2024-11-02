package org.cris6h16;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private RefreshTokenExp refreshTokenExp;
    private AccessTokenExp accessTokenExp;
    @Getter
    @Setter
    public static class RefreshTokenExp {
        private long time;
        private String unit;
    }
    @Getter
    @Setter
    public static class AccessTokenExp {
        private long time;
        private String unit;
    }
}
