package org.cris6h16.user.DTOs;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VerifyEmailDTO {
    private String email;
    private String code;
}
