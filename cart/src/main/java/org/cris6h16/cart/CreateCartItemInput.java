package org.cris6h16.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor // todo: doc:  add porque input y dto son iguales ( haci dice el libro, ya que no tengo suficiente experiencia para dar mi propio criterio me apego a lo que dice el libro)
public class CreateCartItemInput {
    private Long productId;
    private Integer quantity;
}
