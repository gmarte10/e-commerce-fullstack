package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.model.CartItem;
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
    private final CartItemMapper cartItemMapper;


    public CartItemService(CartItemRepository cartItemRepository,
                           CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
    }


    public CartItemDto getCartItemById(Long cartId) {
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

    public CartItemDto createCartItem(CartItemDto cartItemDto) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndProductId(cartItemDto.getUser().getId(), cartItemDto.getProduct().getId());
        if (existingCartItem.isPresent()) {
            int newQuantity = existingCartItem.get().getQuantity() + cartItemDto.getQuantity();
            existingCartItem.get().setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(existingCartItem.get());
            return cartItemMapper.toDto(savedCartItem);
        }
        CartItem cartItem = cartItemRepository.save(cartItemMapper.toEntity(cartItemDto));
        return cartItemMapper.toDto(cartItem);
    }

    public BigDecimal getCartItemsTotalCost(Long userId) {
        return cartItemRepository.findTotalCostByUserId(userId);
    }

    @Transactional
    public void removeCartItemsByUserId(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }


    @Transactional
    public void removeCartItem(CartItemDto cartItemDto) {
        if (cartItemDto.getQuantity() > 1) {
            cartItemDto.setQuantity(cartItemDto.getQuantity() - 1);
            cartItemRepository.save(cartItemMapper.toEntity(cartItemDto));
        }
        else {
            cartItemRepository.delete(cartItemMapper.toEntity(cartItemDto));
        }
    }

    @Transactional
    public CartItemDto getCartItemsByUserIdAndProductId(Long userId, Long productId) {
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Failed to find CartItem By UserId and ProductId");
        }
        return cartItemMapper.toDto(cartItem.get());
    }
}
