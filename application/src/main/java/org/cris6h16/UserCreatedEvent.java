package org.cris6h16;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreatedEvent  {
    private final String email;
}
