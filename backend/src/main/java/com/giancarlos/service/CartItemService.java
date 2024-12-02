package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final CartItemMapper cartItemMapper;


    public CartItemService(CartItemRepository cartItemRepository,
                           ProductService productService,
                           UserService userService,
                           UserMapper userMapper,
                           ProductMapper productMapper,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.productService = productService;
        this.productMapper = productMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public CartItemDto findByCartId(Long cartId) {
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow(() -> new CartItemNotFoundException("Cart Item was not found"));
        return cartItemMapper.cartItemToCartItemDto(cartItem);
    }

    public List<CartItemDto> findByUserId(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            CartItemDto cartItemDto = cartItemMapper.cartItemToCartItemDto(cartItem);
            cartItemDtos.add(cartItemDto);
        }
        return cartItemDtos;
    }

    public CartItemDto addCartItem(CartItemDto cartItemDto) {
        User user = userMapper.userDtoToUser(userService.getUserById(cartItemDto.getUserId()));
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), cartItemDto.getProductId());
        if (existingCartItem.isPresent()) {
            int newQuantity = existingCartItem.get().getQuantity() + cartItemDto.getQuantity();
            existingCartItem.get().setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(existingCartItem.get());
            return cartItemMapper.cartItemToCartItemDto(savedCartItem);
        }
        Product product = productMapper.toEntity(productService.findById(cartItemDto.getProductId()));
        CartItem cartItem = cartItemMapper.cartItemDtoToCartItem(cartItemDto);
        cartItem.setUser(user);
        cartItem.setProduct(product);
        return cartItemMapper.cartItemToCartItemDto(cartItem);
    }

    public BigDecimal getCartTotal(Long userId) {
        return cartItemRepository.getCartTotal(userId);
    }

    @Transactional
    public void clearUserCartItems(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }


    @Transactional
    public void removeProductFromCart(String email, Long productId) {
        UserDto user = userService.getUserByEmail(email);
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId);
        if (cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Cart item was not found");
        }
        else if (cartItem.get().getQuantity() > 1) {
            cartItem.get().setQuantity(cartItem.get().getQuantity() - 1);
            cartItemRepository.save(cartItem.get());
        }
        else {
            cartItemRepository.delete(cartItem.get());
        }
    }
}
