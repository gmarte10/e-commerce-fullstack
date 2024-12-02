package com.giancarlos.mapper.orderItem;

import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    OrderItemDto orderItemTorderItemDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto);

    default Long mapOrderToOrderId(Order order) {
        return order.getId();
    }

    default Long mapProductToProductId(Product product) {
        return product.getId();
    }
}
