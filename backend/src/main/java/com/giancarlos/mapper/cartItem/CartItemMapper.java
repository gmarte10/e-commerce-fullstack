package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    public CartItemMapper(ProductMapper productMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    public CartItemDto toDto(CartItem cartItem) {
        if (cartItem == null) {
            throw new CartItemNotFoundException("CartItem parameter is null in CartItemMapper toDto");
        }
        return CartItemDto.builder()
                .id(cartItem.getId())
                .user(userMapper.toDto(cartItem.getUser()))
                .product(productMapper.toDTO(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .build();
    }

    public CartItem toEntity(CartItemDto cartItemDto) {
        if (cartItemDto == null) {
            throw new UserNotFoundException("CartItem parameter is null in CartItemMapper toEntity");
        }
        return CartItem.builder()
                .id(cartItemDto.getId())
                .user(userMapper.toEntity(cartItemDto.getUser()))
                .product(productMapper.toEntity(cartItemDto.getProduct()))
                .quantity(cartItemDto.getQuantity())
                .build();
    }
}
