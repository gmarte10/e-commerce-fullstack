package com.giancarlos.mapper.user;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Order;
import com.giancarlos.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "orderIds", expression = "java(getOrderIds(user))")
    @Mapping(target = "cartItemsIds", expression = "java(getCartItemIds(user))")
    UserDto userToUserDto(User user);

    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    User userDtoToUser(UserDto userDto);

    default List<Long> getOrderIds(User user) {
        List<Long> orderIds = new ArrayList<>();
        for (Order order : user.getOrders()) {
            orderIds.add(order.getId());
        }
        return orderIds;
    }

    default List<Long> getCartItemIds(User user) {
        List<Long> cartItemIds = new ArrayList<>();
        for (CartItem cartItem : user.getCartItems()) {
            cartItemIds.add(cartItem.getId());
        }
        return cartItemIds;
    }
}
