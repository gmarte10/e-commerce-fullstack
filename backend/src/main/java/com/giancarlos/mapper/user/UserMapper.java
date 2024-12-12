package com.giancarlos.mapper.user;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Order;
import com.giancarlos.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            throw new UserNotFoundException("User parameter is null in UserMapper toDto");
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .address(user.getAddress())
                .phone(user.getPhone())
                .orders(mapOrdersToIds(user.getOrders()))
                .cartItems(mapCartItemsToIds(user.getCartItems()))
                .build();
    }

    private List<Long> mapOrdersToIds(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        List<Long> ret = new ArrayList<>();
        for (Order item : orders) {
            ret.add(item.getId());
        }
        return ret;
    }

    private List<Long> mapCartItemsToIds(List<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }
        List<Long> ret = new ArrayList<>();
        for (CartItem item : cartItems) {
            ret.add(item.getId());
        }
        return ret;
    }
}
