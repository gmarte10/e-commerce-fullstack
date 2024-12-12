package com.giancarlos.mapper.product;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
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
                .cartItems(mapCartItemsToIds(product.getCartItems()))
                .orderItems(mapOrderItemsToIds(product.getOrderItems()))
                .build();
    }

    private List<Long> mapCartItemsToIds(List<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }
        List<Long> ret = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            ret.add(cartItem.getId());
        }
        return ret;
    }

    private List<Long> mapOrderItemsToIds(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        List<Long> ret = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            ret.add(orderItem.getId());
        }
        return ret;
    }
}
