package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.CartItemDto;
import com.giancarlos.dto.UserDto;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Order;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);
}
