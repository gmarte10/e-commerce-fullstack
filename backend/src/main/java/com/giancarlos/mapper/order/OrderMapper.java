package com.giancarlos.mapper.order;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemIds", source = "orderItems")
    OrderDto orderToOrderDto(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order orderDtoToOrder(OrderDto orderDto);

    default List<Long> mapOrderItemsToOrderItemIds(List<OrderItem> orderItems) {
        List<Long> orderItemIds = new ArrayList<>();
        for (OrderItem oItem : orderItems) {
            orderItemIds.add(oItem.getId());
        }
        return orderItemIds;
    }

}
