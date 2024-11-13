package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String name;
}
