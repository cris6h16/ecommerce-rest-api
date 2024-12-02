package org.cris6h16.facades;

import org.cris6h16.cart.CartComponent;
import org.cris6h16.cart.CartItemOutput;
import org.cris6h16.cart.CartOutput;
import org.cris6h16.cart.CreateCartItemInput;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.UserComponent;

import java.util.stream.Collectors;

import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.CART_ITEM_NOT_FOUND;
import static org.cris6h16.facades.FacadesCommon.isUserEnabled;

public class CartFacadeImpl implements CartFacade{
    private final CartComponent cartComponent;
    private final SecurityComponent securityComponent;
    private final UserComponent userComponent;

    public CartFacadeImpl(CartComponent cartComponent, SecurityComponent securityComponent, UserComponent userComponent) {
        this.cartComponent = cartComponent;
        this.securityComponent = securityComponent;
        this.userComponent = userComponent;
    }

    @Override
    public Long addItemToCart(CreateCartItemDTO dto) {
        Long userId = securityComponent.getCurrentUserId();
        isUserEnabled(userId, userComponent);
        return cartComponent.addItemToCart(toInput(dto), userId);
    }

    private CreateCartItemInput toInput(CreateCartItemDTO dto) {
        return CreateCartItemInput.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .build();
    }

    @Override
    public CartDTO getOrCreateMyCart() {
        Long userId = securityComponent.getCurrentUserId();
        isUserEnabled(userId, userComponent);
        return toDTO(cartComponent.getOrCreateCartByUserId(userId));
    }

    private CartDTO toDTO(CartOutput output) {
        return new CartDTO(
                output.getId(),
                output.getItems().stream().map(this::toDTO).collect(Collectors.toSet())
        );
    }

    private CartItemDTO toDTO(CartItemOutput output) {
        return CartItemDTO.builder()
                .id(output.getId())
                .productId(output.getProductId())
                .productName(output.getProductName())
                .productImgUrl(output.getProductImgUrl())
                .quantity(output.getQuantity())
                .price(output.getPrice())
                .total(output.getTotal())
                .build();
    }



    @Override
    public void updateCartItemQuantity(Long itemId, Integer quantity) {
        isUserEnabled(securityComponent.getCurrentUserId(), userComponent);
        cartComponent.updateCartItemQuantityById(quantity, itemId);
    }

    @Override
    public void deleteCartItem(Long itemId) {
        Long userId = securityComponent.getCurrentUserId();
        isUserEnabled(userId, userComponent);
        isOwnerOfCart(userId, itemId);
        cartComponent.deleteCartItemById(itemId);
    }

    private void isOwnerOfCart(Long userId, Long itemId) {
        if (!cartComponent.isOwnerOfCartItem(userId, itemId)) {
            throw new ApplicationException(CART_ITEM_NOT_FOUND);
        }
    }
}
