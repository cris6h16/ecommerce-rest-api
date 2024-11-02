package org.cris6h16;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
public class GenAccessTokenInput {
    private Long id;
    private boolean enabled;
    private Set<String> authorities;
}
