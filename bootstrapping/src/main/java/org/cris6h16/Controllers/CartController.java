package org.cris6h16.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.facades.CartDTO;
import org.cris6h16.facades.CreateCartItemDTO;
import org.cris6h16.facades.CartFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;

@RestController
@RequestMapping(CartController.CART_PATH)
@Slf4j
public class CartController {
    public static final String CART_PATH = PATH_PREFIX + "/cart";
    private final CartFacade cartFacade;

    public CartController(CartFacade cartFacade) {
        this.cartFacade = cartFacade;
    }

    @PostMapping(
            value = "/add",
            consumes = "application/json"
    )
    public ResponseEntity<Void> addProductToCart(@RequestBody CreateCartItemDTO dto, UriComponentsBuilder ucb) {
        URI uri = ucb
                .path(CART_PATH + "/{id}")
                .buildAndExpand(cartFacade.addItemToCart(dto))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(
            value = "/my-cart",
            produces = "application/json"
    )
    public ResponseEntity<CartDTO> getCart() {
        return ResponseEntity.ok(cartFacade.getMyCart());
    }

    @PutMapping(
            value = "/{itemId}",
            consumes = "application/json"
    )
    public ResponseEntity<Void> updateCartItem(@PathVariable Long itemId, @RequestBody CreateCartItemDTO dto) {
        cartFacade.updateCartItem(itemId, dto);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(
            value = "/{itemId}"
    )
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long itemId) {
        cartFacade.deleteCartItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
