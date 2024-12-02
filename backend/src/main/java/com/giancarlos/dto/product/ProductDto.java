package com.giancarlos.dto.product;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String imageURL;
    private List<CartItemDto> cartItems;
    private List<OrderItemDto> orderItems;
}
