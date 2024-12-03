package com.giancarlos.repository;

import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void OrderRepository_Save_ReturnSavedOrder() {
        // Arrange
        User user = User.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password("temp")
                .orders(null)
                .cartItems(null)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(150.75))
                .build();
        // Act
        Order saved = orderRepository.save(order);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0L);
        Assertions.assertThat(saved.getTotalAmount()).isEqualTo(BigDecimal.valueOf(150.75));
    }

    @Test
    public void OrderRepository_FindByUserIdOrderByCreatedAtDesc_ReturnMoreThanOneOrder() {
        // Arrange
        User user = User.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password("temp")
                .orders(null)
                .cartItems(null)
                .build();
        User user2 = User.builder()
                .name("Jane Doe")
                .email("jahnedoe@gmail.com")
                .phone("789-456-7891")
                .address("543 Sesame St")
                .role(UserRole.ADMIN)
                .password("pass")
                .orders(null)
                .cartItems(null)
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        List<OrderItem> orderItems = new ArrayList<>();

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(150.75))
                .build();

        Order order2 = Order.builder()
                .user(user2)
                .orderItems(orderItems)
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(20145.75))
                .build();

        Order order3 = Order.builder()
                .user(user2)
                .orderItems(orderItems)
                .createdAt(ZonedDateTime.now().plusHours(15))
                .totalAmount(BigDecimal.valueOf(3))
                .build();

        orderRepository.save(order2);
        orderRepository.save(order);
        orderRepository.save(order3);

        // Act
        List<Order> orderList = orderRepository.findByUserIdOrderByCreatedAtDesc(user2.getId());
        for (Order o : orderList) {
            System.out.println(o.getCreatedAt());
            System.out.println(o.getTotalAmount());
            System.out.println();
        }

        // Assert
        Assertions.assertThat(orderList).isNotNull();
        Assertions.assertThat(orderList.size()).isEqualTo(2);
        Assertions.assertThat(orderList.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(3));
    }

    @Test
    public void OrderRepository_FindById_ReturnOrder() {
        // Arrange
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = Order.builder()
                .user(null)
                .orderItems(orderItems)
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(150.75))
                .build();
        
        orderRepository.save(order);

        // Act
        Order saved = orderRepository.findById(order.getId()).get();

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0L);
        Assertions.assertThat(saved.getTotalAmount()).isEqualTo(BigDecimal.valueOf(150.75));
    }
}
