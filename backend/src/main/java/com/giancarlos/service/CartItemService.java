package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    public CartItemService(CartItemRepository cartItemRepository,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
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

    public CartItemDto createCartItem(CartItem cartItem) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndProductId(cartItem.getUser().getId(), cartItem.getProduct().getId());
        if (existingCartItem.isPresent()) {
            int newQuantity = existingCartItem.get().getQuantity() + cartItem.getQuantity();
            existingCartItem.get().setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(existingCartItem.get());
            return cartItemMapper.toDto(savedCartItem);
        }
        CartItem saved = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(saved);
    }

    @Transactional
    public void removeCartItemsByUserId(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }


    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        }
        else {
            cartItemRepository.delete(cartItem);
        }
    }

    @Transactional
    public CartItemDto getCartItemByUserIdAndProductId(Long userId, Long productId) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Failed to find CartItem By UserId and ProductId");
        }
        return cartItemMapper.toDto(cartItem.get());
    }
}
