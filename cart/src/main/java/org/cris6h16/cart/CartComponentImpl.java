package org.cris6h16.cart;

import org.antlr.v4.runtime.misc.LogManager;
import org.cris6h16.product.ProductRepository;
import org.cris6h16.user.UserRepository;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.cris6h16.cart.CartComponentErrorCode.CART_ITEM_NOT_FOUND_BY_ID;
import static org.cris6h16.cart.CartComponentErrorCode.PRODUCT_NOT_FOUND_BY_ID;
import static org.cris6h16.cart.CartComponentErrorCode.USER_NOT_FOUND_BY_ID;

public class CartComponentImpl implements CartComponent {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartValidator cartValidator;
    private final CartItemRepository cartItemRepository;

    public CartComponentImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartValidator cartValidator, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartValidator = cartValidator;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Long addItemToCart(CreateCartItemInput input, Long userId) {
        cartValidator.validate(input);
        cartValidator.validateUserId(userId);

        CartEntity ce = findCartByUserIdOrCreate(userId);
        CartItemEntity cie = toEntity(input, ce);
        return cartItemRepository.save(cie).getId();
    }

    private CartItemEntity toEntity(CreateCartItemInput input, CartEntity cartEntity) {
        Supplier<CartComponentException> productNotFound = () -> new CartComponentException(PRODUCT_NOT_FOUND_BY_ID);

        return CartItemEntity.builder()
                .id(null)
                .quantity(input.getQuantity())
                .cart(cartEntity)
                .product(productRepository.findById(input.getProductId()).orElseThrow(productNotFound))
                .build();
    }

    private CartEntity findCartByUserIdOrCreate(Long userId) {
        return cartRepository.findByUserId(userId).orElse(createCart(userId));
    }

    private CartEntity createCart(Long userId) {
        Supplier<CartComponentException> cartNotFound = () -> new CartComponentException(USER_NOT_FOUND_BY_ID);

        return cartRepository.save(CartEntity.builder()
                .user(userRepository.findByIdAndEnabled(userId, true).orElseThrow(cartNotFound))
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
                .productId(e.getProduct().getId())
                .productName(e.getProduct().getName())
                .productImgUrl(e.getProduct().getImageUrls().iterator().next())
                .quantity(e.getQuantity())
                .price(e.getProduct().getPrice())
                .build();
    }


    @Override
    public void deleteCartItemById(Long itemId) {

    }

    @Override
    public void updateCartItemQuantityById(Integer quantity, Long itemId) {
        cartValidator.validateQuantity(quantity);
        cartValidator.validateCartItemId(itemId);

        cartItemRepository.updateQuantityById(quantity, itemId);
    }

    @Override
    public boolean isOwnerOfCartItem(Long userId, Long itemId) {
        cartValidator.validateUserId(userId);
        cartValidator.validateCartItemId(itemId);

        return cartItemRepository.existsByIdAndCartUserId(itemId, userId);
    }
}
