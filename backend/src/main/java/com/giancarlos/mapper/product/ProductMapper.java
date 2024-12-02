package com.giancarlos.mapper.product;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.mapper.orderItem.OrderItemMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
    private final CartItemMapper cartItemMapper;
    private final OrderItemMapper orderItemMapper;

    public ProductMapper(CartItemMapper cartItemMapper, OrderItemMapper orderItemMapper) {
        this.cartItemMapper = cartItemMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public ProductDto toDto(Product product) {
        if (product == null) {
            throw new ProductNotFoundException("Product parameter not found in ProductMapper toDto");
        }
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory())
                .imageURL(product.getImageURL())
                .cartItems(mapCartItemsToDtos(product.getCartItems()))
                .orderItems(mapOrderItemsToDtos(product.getOrderItems()))
                .build();
    }

    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            throw new ProductNotFoundException("ProductDto parameter not found in ProductMapper toDto");
        }
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .category(productDto.getCategory())
                .imageURL(productDto.getImageURL())
                .cartItems(mapDtosToCartItems(productDto.getCartItems()))
                .orderItems(mapDtosToOrderItems(productDto.getOrderItems()))
                .build();
    }

    private List<CartItemDto> mapCartItemsToDtos(List<CartItem> cartItems) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartItemDtos.add(cartItemMapper.toDto(cartItem));
        }
        return cartItemDtos;
    }

    private List<OrderItemDto> mapOrderItemsToDtos(List<OrderItem> orderItems) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(orderItemMapper.toDto(orderItem));
        }
        return orderItemDtos;
    }

    private List<CartItem> mapDtosToCartItems(List<CartItemDto> cartItemDtos) {
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemDto cartItemDto : cartItemDtos) {
            cartItems.add(cartItemMapper.toEntity(cartItemDto));
        }
        return cartItems;
    }

    private List<OrderItem> mapDtosToOrderItems(List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            orderItems.add(orderItemMapper.toEntity(orderItemDto));
        }
        return orderItems;
    }

}
