package org.cris6h16.facades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartItemDTO {
    private Long productId;
    private Integer quantity;
}
