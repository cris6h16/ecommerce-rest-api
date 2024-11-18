package org.cris6h16.product;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCategoryInput implements Prepareable{
    private String name;

    @Override
    public void trim() {
        name = name.trim();
    }

    @Override
    public void nullToEmpty() {
        name = name == null ? "" : name;
    }
    // todo: add id of the creator( track who created the category can be banned if needed)
}
