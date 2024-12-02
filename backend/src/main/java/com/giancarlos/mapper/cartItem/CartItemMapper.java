package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);
}
