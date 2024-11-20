package com.giancarlos.service;

import com.giancarlos.dto.CartItemDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.ProductMapper;
import com.giancarlos.mapper.UserMapper;
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
    private ProductService productService;
    private UserService userService;
    private UserMapper userMapper;
    private ProductMapper productMapper;

    public CartItemService(CartItemRepository cartItemRepository,
                           ProductService productService,
                           UserService userService,
                           UserMapper userMapper,
                           ProductMapper productMapper) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemDto> findByUserId(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem c : cartItems) {
            Long pId = c.getUser().getId();
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setUserId(userId);
            cartItemDto.setProductId(pId);
            cartItemDto.setQuantity(c.getQuantity());
        }
        return cartItemDtos;
    }

    public CartItemDto addCartItem(CartItemDto cartItemDto) {
        CartItem cartItem = new CartItem();
        User user = userMapper.toEntity(userService.getUserById(cartItemDto.getUserId()));
        Product product = productMapper.toEntity(productService.findById(cartItemDto.getProductId()));
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        CartItemDto ciDto = new CartItemDto();
        ciDto.setProductId(savedCartItem.getProduct().getId());
        ciDto.setUserId(savedCartItem.getUser().getId());
        ciDto.setQuantity(savedCartItem.getQuantity());
        return ciDto;
    }

    public BigDecimal getCartTotal(Long userId) {
        return cartItemRepository.getCartTotal(userId);
    }

    @Transactional
    public void clearUserCartItems(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    @Transactional
    public void removeProductFromCart(CartItemDto cartItemDto) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(cartItemDto.getUserId(), cartItemDto.getProductId());
        if (cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Cart item was not found");
        }
        cartItemRepository.delete(cartItem.get());
    }





}
