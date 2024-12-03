package com.giancarlos.service;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.user.UserDto;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Order order2;
    private OrderDto orderDto;
    private OrderDto orderDto2;

    @BeforeEach
    public void init() {


        order = Order.builder()
                .user(User.builder().id(1L).build())
                .orderItems(null)
                .createdAt(ZonedDateTime.now().plusHours(10))
                .totalAmount(BigDecimal.valueOf(150.75))
                .build();

        order2 = Order.builder()
                .user(User.builder().id(2L).build())
                .orderItems(null)
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(20145.75))
                .build();

        orderDto = OrderDto.builder()
                .user(UserDto.builder().id(1L).build())
                .orderItems(null)
                .createdAt(ZonedDateTime.now().plusHours(10))
                .totalAmount(BigDecimal.valueOf(150.75))
                .build();

        orderDto2 = OrderDto.builder()
                .user(UserDto.builder().id(2L).build())
                .orderItems(null)
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(20145.75))
                .build();
    }

    @Test
    public void OrderService_GetOrdersByUserId_ReturnMoreThanOneOrder() {
        // Arrange
        Long userId = 1L;
        when(orderRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(order));
        when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(orderDto);

        // Act
        List<OrderDto> orderDtoList = orderService.getOrdersByUserId(userId);

        // Assert
        Assertions.assertThat(orderDtoList).isNotNull();
        Assertions.assertThat(orderDtoList.size()).isEqualTo(1);
        Assertions.assertThat(orderDtoList.get(0)).isEqualTo(orderDto);
    }

    @Test
    public void OrderService_GetOrderById_ReturnOrder() {
        // Arrange
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(Optional.ofNullable(order));
        when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(orderDto);

        // Act
        OrderDto found = orderService.getOrderById(id);

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found).isEqualTo(orderDto);
    }

    @Test
    public void OrderService_CreateOrder_ReturnOrder() {
        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        when(orderMapper.toEntity(Mockito.any(OrderDto.class))).thenReturn(order);
        when(orderMapper.toDto(Mockito.any(Order.class))).thenReturn(orderDto);
        OrderDto saved = orderService.createOrder(orderDto);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getTotalAmount()).isEqualTo(order.getTotalAmount());
    }
}
