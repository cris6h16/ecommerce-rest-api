package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCategoryDTO {
    private String name;
}
