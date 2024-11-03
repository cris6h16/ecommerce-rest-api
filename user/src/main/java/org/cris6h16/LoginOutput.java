package org.cris6h16;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginOutput {
    private String accessToken;
    private String refreshToken;
}
