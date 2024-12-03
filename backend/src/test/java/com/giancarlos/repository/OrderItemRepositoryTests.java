package com.giancarlos.repository;

import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderItemRepositoryTests {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void OrderItemRepository_Save_ReturnOrderItem() {
        // Arrange
        OrderItem orderItem = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(null)
                .order(null)
                .build();
        // Act
        OrderItem saved = orderItemRepository.save(orderItem);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0L);
    }

    @Test
    public void OrderItemRepository_FindById_ReturnOrderItem() {
        // Arrange
        OrderItem orderItem = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(null)
                .order(null)
                .build();
        orderItemRepository.save(orderItem);

        // Act
        OrderItem found = orderItemRepository.findById(orderItem.getId()).get();

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getId()).isGreaterThan(0L);
    }

    @Test
    public void OrderItemRepository_FindByOrderId_ReturnMoreThanOneOrderItem() {
        // Arrange
        Order order = Order.builder()
                .createdAt(ZonedDateTime.now())
                .totalAmount(BigDecimal.valueOf(42))
                .user(null)
                .orderItems(null)
                .build();
        Order order2 = Order.builder()
                .createdAt(ZonedDateTime.now().plusHours(45))
                .totalAmount(BigDecimal.valueOf(3))
                .user(null)
                .orderItems(null)
                .build();
        orderRepository.save(order);
        orderRepository.save(order2);

        OrderItem orderItem = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(null)
                .order(order)
                .build();
        OrderItem orderItem2 = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(null)
                .order(order2)
                .build();
        orderItemRepository.save(orderItem);
        orderItemRepository.save(orderItem2);

        // Act
        List<OrderItem> orderItemList = orderItemRepository.findByOrderId(order.getId());

        // Assert
        Assertions.assertThat(orderItemList).isNotNull();
        Assertions.assertThat(orderItemList.size()).isEqualTo(1);
    }

    @Test
    public void OrderItemRepository_FindByProductId_ReturnMoreThanOneOrderItem() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();

        Product product2 = Product.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);
        productRepository.save(product2);

        OrderItem orderItem = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(product)
                .order(null)
                .build();
        OrderItem orderItem2 = OrderItem.builder()
                .price(BigDecimal.valueOf(45))
                .quantity(4)
                .product(product2)
                .order(null)
                .build();
        orderItemRepository.save(orderItem);
        orderItemRepository.save(orderItem2);

        // Act
        List<OrderItem> orderItemList = orderItemRepository.findByProductId(product.getId());

        // Assert
        Assertions.assertThat(orderItemList).isNotNull();
        Assertions.assertThat(orderItemList.size()).isEqualTo(1);
    }
}
