package org.cris6h16.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginOutput {
    private String accessToken;
    private String refreshToken;
}
