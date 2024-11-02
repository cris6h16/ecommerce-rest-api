package org.cris6h16.Events;


import lombok.Getter;

@Getter
public class EmailVerificationRequiredEvent  {
    private String email;
}
