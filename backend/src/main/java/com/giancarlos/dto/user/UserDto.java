package com.giancarlos.dto.user;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String address;
    private String phone;
    private List<Long> orders;
    private List<Long> cartItems;
}
