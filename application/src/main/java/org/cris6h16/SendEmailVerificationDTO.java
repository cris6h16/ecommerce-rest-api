package org.cris6h16;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cris6h16.facades.EmailCodeActionType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SendEmailVerificationDTO {
    private String email;
    private EmailCodeActionType actionType;
}
