package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.cartItem.CartItemRequestDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.service.ProductService;
import com.giancarlos.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class CartItemRequestMapper {
    private final UserService userService;
    private final ProductService productService;

    public CartItemRequestMapper(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;

    }
    public CartItemDto toDto(CartItemRequestDto cartItemRequestDto) {
        if (cartItemRequestDto == null) {
            throw new CartItemNotFoundException("CartItemRequestDto is null in CartItemRequestMapper in toDto");
        }
        return CartItemDto.builder()
                .quantity(cartItemRequestDto.getQuantity())
                .product(productService.getProductById(cartItemRequestDto.getProductId()))
                .user(userService.getUserByEmail(cartItemRequestDto.getEmail()))
                .build();
    }
}
