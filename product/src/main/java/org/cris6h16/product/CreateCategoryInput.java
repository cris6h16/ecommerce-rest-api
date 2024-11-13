package org.cris6h16.product;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCategoryInput {
    private String name;
    // todo: add id of the creator( track who created the category can be banned if needed)
}
