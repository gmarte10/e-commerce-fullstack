package com.giancarlos.service;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.mapper.orderItem.OrderItemMapper;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.repository.OrderItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTests {
    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderItem orderItem;
    private OrderItem orderItem2;
    private OrderItemDto orderItemDto;
    private OrderItemDto orderItemDto2;

    @BeforeEach
    public void init() {
        orderItem = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(null)
                .order(null)
                .build();
        orderItem2 = OrderItem.builder()
                .price(BigDecimal.valueOf(12))
                .quantity(7)
                .product(null)
                .order(null)
                .build();

        orderItemDto = OrderItemDto.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(null)
                .order(null)
                .build();
        orderItemDto2 = OrderItemDto.builder()
                .price(BigDecimal.valueOf(12))
                .quantity(7)
                .product(null)
                .order(null)
                .build();
    }

    @Test
    public void OrderItemService_GetOrderItemById_ReturnOrderItem() {
        // Arrange
        Long id = 1L;
        when(orderItemRepository.findById(id)).thenReturn(Optional.ofNullable(orderItem));
        when(orderItemMapper.toDto(Mockito.any(OrderItem.class))).thenReturn(orderItemDto);

        // Act
        OrderItemDto found = orderItemService.getOrderItemById(id);

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found).isEqualTo(orderItemDto);
    }

    @Test
    public void OrderItemService_GetOrderItemsByOrderId_ReturnMoreThanOneOrderItem() {
        // Arrange
        Long orderId = 1L;
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(List.of(orderItem));
        when(orderItemMapper.toDto(Mockito.any(OrderItem.class))).thenReturn(orderItemDto);

        // Act
        List<OrderItemDto> orderDtoList = orderItemService.getOrderItemsByOrderId(orderId);

        // Assert
        Assertions.assertThat(orderDtoList).isNotNull();
        Assertions.assertThat(orderDtoList.size()).isEqualTo(1);
        Assertions.assertThat(orderDtoList.get(0)).isEqualTo(orderItemDto);
    }

    @Test
    public void OrderItemService_GetOrderItemsByProductId_ReturnMoreThanOneOrderItem() {
        // Arrange
        Long productId = 1L;
        when(orderItemRepository.findByProductId(productId)).thenReturn(List.of(orderItem));
        when(orderItemMapper.toDto(Mockito.any(OrderItem.class))).thenReturn(orderItemDto);

        // Act
        List<OrderItemDto> orderDtoList = orderItemService.getOrderItemsByProductId(productId);

        // Assert
        Assertions.assertThat(orderDtoList).isNotNull();
        Assertions.assertThat(orderDtoList.size()).isEqualTo(1);
        Assertions.assertThat(orderDtoList.get(0)).isEqualTo(orderItemDto);
    }

    @Test
    public void OrderItemService_CreateOrderItem_ReturnOrderItem() {
        when(orderItemRepository.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);
        when(orderItemMapper.toEntity(Mockito.any(OrderItemDto.class))).thenReturn(orderItem);
        when(orderItemMapper.toDto(Mockito.any(OrderItem.class))).thenReturn(orderItemDto);
        OrderItemDto saved = orderItemService.createOrderItem(orderItemDto);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getPrice()).isEqualTo(orderItemDto.getPrice());
    }

}
