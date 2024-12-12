package com.giancarlos.service;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.exception.OrderItemNotFoundException;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTests {
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemService orderItemService;

    @Test
    void OrderItemService_GetOrderItemById_ReturnOrderItemDto_Success() {
        Long orderItemId = 1L;
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemId);

        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.getOrderItemById(orderItemId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(orderItemId);
        verify(orderItemRepository, times(1)).findById(orderItemId);
    }

    @Test
    void OrderItemService_GetOrderItemById_ReturnOrderItemDto_NotFound() {
        Long orderItemId = 1L;

        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.getOrderItemById(orderItemId));
        verify(orderItemRepository, times(1)).findById(orderItemId);
    }

    @Test
    void OrderItemService_GetOrderItemsByOrderId_ReturnMoreThanOneOrderItem_Success() {
        Long orderId = 1L;
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItems.add(orderItem);

        OrderItemDto orderItemDto = new OrderItemDto();

        when(orderItemRepository.findByOrderId(orderId)).thenReturn(orderItems);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        List<OrderItemDto> result = orderItemService.getOrderItemsByOrderId(orderId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(orderItemRepository, times(1)).findByOrderId(orderId);
        verify(orderItemMapper, times(1)).toDto(orderItem);
    }

    @Test
    void OrderItemService_GetOrderItemsByOrderId_ReturnMoreThanOneOrderItem_EmptyList() {
        Long orderId = 1L;

        when(orderItemRepository.findByOrderId(orderId)).thenReturn(new ArrayList<>());

        List<OrderItemDto> result = orderItemService.getOrderItemsByOrderId(orderId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isEmpty()).isTrue();
        verify(orderItemRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void OrderItemService_CreateOrderItem_ReturnOrderItem_Success() {
        OrderItem orderItem = new OrderItem();
        OrderItem savedOrderItem = new OrderItem();
        OrderItemDto orderItemDto = new OrderItemDto();

        when(orderItemRepository.save(orderItem)).thenReturn(savedOrderItem);
        when(orderItemMapper.toDto(savedOrderItem)).thenReturn(orderItemDto);

        OrderItemDto result = orderItemService.createOrderItem(orderItem);

        Assertions.assertThat(result).isNotNull();
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderItemMapper, times(1)).toDto(savedOrderItem);
    }
}
