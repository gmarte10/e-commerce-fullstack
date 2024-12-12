package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.cartItem.CartItemResponseDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.model.Product;
import com.giancarlos.service.ImageService;
import com.giancarlos.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class CartItemResponseMapper {
    private final ProductService productService;
    private final ImageService imageService;

    public CartItemResponseMapper(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    public CartItemResponseDto toResponse(CartItemDto cartItemDto) {
        if (cartItemDto == null) {
            throw new CartItemNotFoundException("CartItemDto parameter is null in CartItemResponseMapper in toResponse");
        }
        Product product = productService.getProductById(cartItemDto.getProductId());
        return CartItemResponseDto.builder()
                .id(cartItemDto.getId())
                .name(product.getName())
                .category(product.getCategory())
                .description(product.getDescription())
                .imgBase64(imageService.getImageBase64(product.getImageURL()))
                .quantity(cartItemDto.getQuantity())
                .price(product.getPrice())
                .build();
    }
}
