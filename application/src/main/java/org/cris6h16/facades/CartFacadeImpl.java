package org.cris6h16.facades;

import org.cris6h16.cart.CartComponent;
import org.cris6h16.cart.CartItemOutput;
import org.cris6h16.cart.CartOutput;
import org.cris6h16.cart.CreateCartItemInput;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.product.ProductOutput;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.UserComponent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.CART_ITEM_NOT_FOUND;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.INSUFFICIENT_STOCK;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.PRODUCT_NOT_FOUND;

@Component
public class CartFacadeImpl implements CartFacade {
    private final CartComponent cartComponent;
    private final ProductComponent productComponent;
    private final SecurityComponent securityComponent;
    private final UserComponent userComponent;

    public CartFacadeImpl(CartComponent cartComponent, ProductComponent productComponent, SecurityComponent securityComponent, UserComponent userComponent) {
        this.cartComponent = cartComponent;
        this.productComponent = productComponent;
        this.securityComponent = securityComponent;
        this.userComponent = userComponent;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Long addItemToCart(CreateCartItemDTO dto) {
        Long userId = securityComponent.getCurrentUserId();
        productExists(dto.getProductId());
        enoughStock(dto.getProductId(), dto.getQuantity());
        return cartComponent.addItemToCart(toInput(dto), userId);
    }

    private void enoughStock(Long productId, Integer quantity) {
        Integer stock = productComponent.findProductStockById(productId);
        if (stock < quantity) {
            throw new ApplicationException(INSUFFICIENT_STOCK);
        }
    }

    private void productExists(Long productId) {
        if (productComponent.existProductById(productId)) {
            throw new ApplicationException(PRODUCT_NOT_FOUND);
        }
    }

    private CreateCartItemInput toInput(CreateCartItemDTO dto) {
        return CreateCartItemInput.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public CartDTO getOrCreateMyCart() {
        Long userId = securityComponent.getCurrentUserId();
        return toDTO(cartComponent.getOrCreateCartByUserId(userId));
    }

    private CartDTO toDTO(CartOutput output) {
        return new CartDTO(
                output.getId(),
                output.getItems().stream().map(this::toDTO).collect(Collectors.toSet())
        );
    }

    private CartItemDTO toDTO(CartItemOutput output) {
        ProductOutput product = getProductOutput(output);

        return CartItemDTO.builder()
                .id(output.getId())
                .productId(output.getProductId())
                .productName(product.getName())
                .productImgUrl(product.getImageUrls())
                .quantity(output.getQuantity())
                .price(product.getPrice())
                .total(product.getPrice().multiply(BigDecimal.valueOf(output.getQuantity())))
                .build();
    }

    private ProductOutput getProductOutput(CartItemOutput output) {
        return productComponent.findProductById(output.getProductId());
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateCartItemQuantity(Long itemId, Integer delta) {
        isOwnerOfCartItem(securityComponent.getCurrentUserId(), itemId); // todo: hacer verificaciones como estas

        Long productId = cartComponent.findProductIdByItemId(itemId);
        int stock = productComponent.findProductStockById(productId);

        cartComponent.updateCartItemQuantityById(delta, itemId, stock);
    }

    @Override
    public void deleteCartItem(Long itemId) {
        Long userId = securityComponent.getCurrentUserId();
        isOwnerOfCartItem(userId, itemId);
        cartComponent.deleteCartItemById(itemId);
    }

    private void isOwnerOfCartItem(Long userId, Long itemId) {
        if (!cartComponent.isOwnerOfCartItem(userId, itemId)) {
            throw new ApplicationException(CART_ITEM_NOT_FOUND);
        }
    }
}
