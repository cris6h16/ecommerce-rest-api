package org.cris6h16.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryOutput {
    private String name;
    private Long id;
}
