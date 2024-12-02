package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.CartItem;
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
    private final UserService userService;
    private final UserMapper userMapper;
    private final CartItemMapper cartItemMapper;


    public CartItemService(CartItemRepository cartItemRepository,
                           UserService userService,
                           UserMapper userMapper,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.cartItemMapper = cartItemMapper;
    }


    public CartItemDto getCartItemByCartId(Long cartId) {
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow(() -> new CartItemNotFoundException("Cart Item was not found"));
        return cartItemMapper.toDto(cartItem);
    }

    public List<CartItemDto> getCartItemsByProductId(Long productId) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
        for (CartItem cartItem : cartItems) {
            cartItemDtos.add(cartItemMapper.toDto(cartItem));
        }
        return cartItemDtos;
    }

    public List<CartItemDto> getCartItemsByUserId(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            CartItemDto cartItemDto = cartItemMapper.toDto(cartItem);
            cartItemDtos.add(cartItemDto);
        }
        return cartItemDtos;
    }

    public CartItemDto addCartItem(CartItemDto cartItemDto) {
        User user = userMapper.toEntity(userService.getUserById(cartItemDto.getUser().getId()));
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), cartItemDto.getProduct().getId());
        if (existingCartItem.isPresent()) {
            int newQuantity = existingCartItem.get().getQuantity() + cartItemDto.getQuantity();
            existingCartItem.get().setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(existingCartItem.get());
            return cartItemMapper.toDto(savedCartItem);
        }
        CartItem cartItem = cartItemMapper.toEntity(cartItemDto);
        return cartItemMapper.toDto(cartItem);
    }

    public BigDecimal getCartTotalCost(Long userId) {
        return cartItemRepository.findTotalCostByUserId(userId);
    }

    @Transactional
    public void removeCartItemsByUserId(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }


    @Transactional
    public void removeCartItemByEmailAndProductId(String email, Long productId) {
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
