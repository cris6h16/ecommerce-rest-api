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


@RestController
@RequestMapping(CartController.CART_PATH)
@Slf4j
public class CartController {
    public static final String CART_PATH =  "api/v1/cart";
    public static final String CART_ITEM_SUB_PATH =  "/item";
    private final CartFacade cartFacade;

    public CartController(CartFacade cartFacade) {
        this.cartFacade = cartFacade;
    }

    @GetMapping(
            value = "/my-cart",
            produces = "application/json"
    )
    public ResponseEntity<CartDTO> getCart() {
        return ResponseEntity.ok(cartFacade.getOrCreateMyCart());
    }

    @PostMapping(
            value = CART_ITEM_SUB_PATH + "/add",
            consumes = "application/json"
    )
    public ResponseEntity<Void> addProductToCart(@RequestBody CreateCartItemDTO dto, UriComponentsBuilder ucb) {
        URI uri = ucb
                .path(CART_ITEM_SUB_PATH + "/{id}")
                .buildAndExpand(cartFacade.addItemToCart(dto))
                .toUri();
        return ResponseEntity.created(uri).build();
    }


    @PutMapping(
            value = CART_ITEM_SUB_PATH + "/{itemId}/amount",
            consumes = "application/json"
    )
    public ResponseEntity<Void> updateCartItem(@PathVariable Long itemId, @RequestBody Integer delta) {
        cartFacade.updateCartItemQuantity(itemId, delta);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(
            value = CART_ITEM_SUB_PATH + "/{itemId}",
            consumes = "application/json"
    )
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long itemId) {
        cartFacade.deleteCartItem(itemId);
        return ResponseEntity.noContent().build();
    }


}
