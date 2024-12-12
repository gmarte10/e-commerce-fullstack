package com.giancarlos.service;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.mapper.order.OrderMapper;
import com.giancarlos.model.Order;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import com.giancarlos.repository.OrderRepository;
import com.giancarlos.repository.UserRepository;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void OrderService_GetOrdersByUserId_ReturnMoreThanOneOrder_Success() {
        Long userId = 1L;
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        orders.add(order);

        OrderDto orderDto = new OrderDto();

        when(orderRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(orders);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(orderRepository, times(1)).findByUserIdOrderByCreatedAtDesc(userId);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    void OrderService_GetOrdersByUserId_ReturnMoreThanOneOrder_EmptyList() {
        Long userId = 1L;

        when(orderRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(new ArrayList<>());

        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isEmpty()).isTrue();
        verify(orderRepository, times(1)).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    void OrderService_GetOrderById_ReturnOrderDto_Success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(orderId);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void OrderService_GetOrderById_ReturnOrderDto_NotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void OrderService_CreateOrder_ReturnOrderDto_Success() {
        Order order = new Order();
        Order savedOrder = new Order();
        OrderDto orderDto = new OrderDto();

        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.toDto(savedOrder)).thenReturn(orderDto);

        OrderDto result = orderService.createOrder(order);

        Assertions.assertThat(result).isNotNull();
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDto(savedOrder);
    }
}
