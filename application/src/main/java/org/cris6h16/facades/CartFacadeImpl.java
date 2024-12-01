package org.cris6h16.facades;

import org.cris6h16.cart.CartComponent;
import org.cris6h16.cart.CreateCartItemInput;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.UserComponent;

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
        Long id = securityComponent.getCurrentUserId();
        isUserEnabled(id, userComponent);
        return cartComponent.addItemToCart(toInput(dto), id);
    }

    private CreateCartItemInput toInput(CreateCartItemDTO dto) {
        return CreateCartItemInput.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .build();
    }

    @Override
    public CartDTO getMyCart() {
        return null;
    }

    @Override
    public void updateCartItem(Long itemId, CreateCartItemDTO dto) {

    }

    @Override
    public void deleteCartItem(Long itemId) {

    }
}
