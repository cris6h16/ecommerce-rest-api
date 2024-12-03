package org.cris6h16.facades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SendEmailVerificationDTO {
    private String email;
    private EmailCodeActionType actionType;
}
