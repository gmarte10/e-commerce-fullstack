package com.giancarlos.repository;

import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartItemRepositoryTests {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void CartItemRepository_Save_ReturnCartItem() {
        // Arrange
        CartItem cartItem = CartItem.builder()
                .user(null)
                .quantity(5)
                .product(null)
                .build();
        // Act
        CartItem saved = cartItemRepository.save(cartItem);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0L);
    }

    @Test
    public void CartItemRepository_FindById_ReturnCartItem() {
        // Arrange
        CartItem cartItem = CartItem.builder()
                .user(null)
                .quantity(5)
                .product(null)
                .build();
        cartItemRepository.save(cartItem);
        // Act
        CartItem found = cartItemRepository.findById(cartItem.getId()).get();

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getId()).isGreaterThan(0L);
    }

    @Test
    public void CartItemRepository_Delete_ReturnNothing() {
        // Arrange
        CartItem cartItem = CartItem.builder()
                .user(null)
                .quantity(5)
                .product(null)
                .build();
        cartItemRepository.save(cartItem);

        // Act
        cartItemRepository.delete(cartItem);
        Optional<CartItem> found = cartItemRepository.findById(cartItem.getId());

        // Assert
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void CartItemRepository_DeleteByUserId_ReturnNothing() {
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
        userRepository.save(user);

        CartItem cartItem = CartItem.builder()
                .user(user)
                .quantity(5)
                .product(null)
                .build();
        cartItemRepository.save(cartItem);

        // Act
        cartItemRepository.deleteByUserId(user.getId());
        Optional<CartItem> found = cartItemRepository.findById(cartItem.getId());

        // Assert
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void CartItemRepository_FindByUserId_ReturnMoreThanOneCartItem() {
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

        CartItem cartItem = CartItem.builder()
                .user(user)
                .quantity(5)
                .product(null)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .user(user2)
                .quantity(5)
                .product(null)
                .build();
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem2);

        // Act
        List<CartItem> cartItemList = cartItemRepository.findByUserId(user.getId());

        // Assert
        Assertions.assertThat(cartItemList).isNotNull();
        Assertions.assertThat(cartItemList.size()).isEqualTo(1);
    }

    @Test
    public void CartItemRepository_FindByProductId_ReturnMoreThanOneCartItem() {
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

        CartItem cartItem = CartItem.builder()
                .user(null)
                .quantity(5)
                .product(product)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .user(null)
                .quantity(5)
                .product(product2)
                .build();
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem2);

        // Act
        List<CartItem> cartItemList = cartItemRepository.findByProductId(product.getId());

        // Assert
        Assertions.assertThat(cartItemList).isNotNull();
        Assertions.assertThat(cartItemList.size()).isEqualTo(1);
    }

    @Test
    public void CartItemRepository_FindByUserIdAndProductId_ReturnCartItem() {
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

        CartItem cartItem = CartItem.builder()
                .user(user)
                .quantity(5)
                .product(product)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .user(user2)
                .quantity(5)
                .product(product2)
                .build();
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem2);

        // Act
        CartItem found = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId()).get();

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getId()).isGreaterThan(0L);
    }

    @Test
    public void CartItemRepository_FindTotalCostByUserId_ReturnBigDecimal() {
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
        userRepository.save(user);

        BigDecimal price = BigDecimal.valueOf(10);
        BigDecimal price2 = BigDecimal.valueOf(20);

        Product product = Product.builder()
                .name("hoodie")
                .price(price)
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        Product product2 = Product.builder()
                .name("laptop")
                .price(price2)
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);
        productRepository.save(product2);


        CartItem cartItem = CartItem.builder()
                .user(user)
                .quantity(5)
                .product(product)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .user(user)
                .quantity(2)
                .product(product2)
                .build();
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem2);

        // Act
        BigDecimal total = cartItemRepository.findTotalCostByUserId(user.getId());
        BigDecimal expected = (price.multiply(BigDecimal.valueOf(5))).add((price2.multiply(BigDecimal.valueOf(2))));

        // Assert
        Assertions.assertThat(total).isNotNull();
        Assertions.assertThat(total.compareTo(expected)).isEqualTo(0);
    }
}
