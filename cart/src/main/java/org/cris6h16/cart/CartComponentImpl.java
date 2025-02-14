package org.cris6h16.cart;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static org.cris6h16.cart.CartComponentErrorCode.CART_ITEM_NOT_FOUND;
import static org.cris6h16.cart.CartComponentErrorCode.INSUFFICIENT_STOCK;
import static org.cris6h16.cart.CartComponentErrorCode.PRODUCT_ALREADY_IN_CART;

@Component
public class CartComponentImpl implements CartComponent {
    private final CartRepository cartRepository;
    private final CartValidator cartValidator;
    private final CartItemRepository cartItemRepository;

    public CartComponentImpl(CartRepository cartRepository,
                             CartValidator cartValidator,
                             CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartValidator = cartValidator;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Long addItemToCart(CreateCartItemInput input, Long userId) {
        cartValidator.validate(input);
        cartValidator.validateUserId(userId);

        productIdUserIdUniqueCartItem(input.getProductId(), userId);

        CartEntity cart = findCartByUserIdOrCreate(userId);
        CartItemEntity cartItem = toEntity(input, cart);
        return cartItemRepository.save(cartItem).getId();
    }

    private void productIdUserIdUniqueCartItem(Long productId, Long userId) {
        if (cartItemRepository.existsByProductIdAndCartUserId(productId, userId)) {
            throw new CartComponentException(PRODUCT_ALREADY_IN_CART);
        }
    }

    private CartItemEntity toEntity(CreateCartItemInput input, CartEntity cartEntity) {
        return CartItemEntity.builder()
                .id(null)
                .quantity(input.getQuantity())
                .cart(cartEntity)
                .productId(input.getProductId())
                .build();
    }

    private CartEntity findCartByUserIdOrCreate(Long userId) {
        return cartRepository
                .findByUserId(userId)
                .orElse(createCart(userId));
    }

    private CartEntity createCart(Long userId) {
        return cartRepository.save(CartEntity.builder()
                .userId(userId)
                .id(null)
                .build());
    }

    @Override
    public CartOutput getOrCreateCartByUserId(Long userId) {
        cartValidator.validateUserId(userId);

        return cartRepository.findByUserId(userId)
                .map(this::toOutput)
                .orElseGet(() -> toOutput(createCart(userId)));
    }

    private CartOutput toOutput(CartEntity cartEntity) {
        return new CartOutput(
                cartEntity.getId(),
                findByCartId(cartEntity.getId())
        );
    }

    private Set<CartItemOutput> findByCartId(Long id) {
        return cartItemRepository.findByCartId(id)
                .stream()
                .map(this::toCartItemOutput)
                .collect(Collectors.toSet());
    }

    private CartItemOutput toCartItemOutput(CartItemEntity e) {
        return CartItemOutput.builder()
                .id(e.getId())
                .productId(e.getProductId())
                .quantity(e.getQuantity())
                .build();
    }


    @Override
    public void deleteCartItemById(Long itemId) {

    }

    @Override
    public void updateCartItemQuantityById(Integer delta, Long itemId, int stock) {
        cartValidator.validateDelta(delta);
        cartValidator.validateCartItemId(itemId);

        int currentQuantity = cartItemRepository.findById(itemId)
                .map(CartItemEntity::getQuantity)
                .orElseThrow(() -> new CartComponentException(CART_ITEM_NOT_FOUND));
        int newQuantity = currentQuantity + delta;

        if (newQuantity <=0){
            cartItemRepository.deleteById(itemId);
            return;
        }

        if (newQuantity > stock) {
            throw new CartComponentException(INSUFFICIENT_STOCK);
        }

        cartItemRepository.updateQuantityById(newQuantity, itemId);
    }

    @Override
    public boolean isOwnerOfCartItem(Long userId, Long itemId) {
        cartValidator.validateUserId(userId);
        cartValidator.validateCartItemId(itemId);

        return cartItemRepository.existsByIdAndCartUserId(itemId, userId);
    }

    @Override
    public Long findProductIdByItemId(Long itemId) {
        return cartItemRepository
                .findProductIdById(itemId)
                .orElseThrow(()-> new CartComponentException(CART_ITEM_NOT_FOUND));
    }
}
