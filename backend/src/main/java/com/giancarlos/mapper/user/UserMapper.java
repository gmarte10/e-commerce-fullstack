package com.giancarlos.mapper.user;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.mapper.order.OrderMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Order;
import com.giancarlos.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    private final OrderMapper orderMapper;
    private final CartItemMapper cartItemMapper;

    public UserMapper(OrderMapper orderMapper, CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
        this.orderMapper = orderMapper;
    }

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
                .orders(mapOrdersToDtos(user.getOrders()))
                .cartItems(mapCartItemsToDtos(user.getCartItems()))
                .build();
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            throw new UserNotFoundException("UserDto parameter is null in UserMapper toEntity");
        }
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .address(userDto.getAddress())
                .phone(userDto.getPhone())
                .orders(mapDtosToOrders(userDto.getOrders()))
                .cartItems(mapDtosToCartItems(userDto.getCartItems()))
                .build();
    }

    private List<OrderDto> mapOrdersToDtos(List<Order> orders) {
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(orderMapper.toDto(order));
        }
        return orderDtos;
    }

    private List<CartItemDto> mapCartItemsToDtos(List<CartItem> cartItems) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartItemDtos.add(cartItemMapper.toDto(cartItem));
        }
        return cartItemDtos;
    }

    private List<Order> mapDtosToOrders(List<OrderDto> orderDtos) {
        List<Order> orders = new ArrayList<>();
        for (OrderDto orderDto : orderDtos) {
            orders.add(orderMapper.toEntity(orderDto));
        }
        return orders;
    }

    private List<CartItem> mapDtosToCartItems(List<CartItemDto> cartItemDtos) {
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemDto cartItemDto : cartItemDtos) {
            cartItems.add(cartItemMapper.toEntity(cartItemDto));
        }
        return cartItems;
    }
}
