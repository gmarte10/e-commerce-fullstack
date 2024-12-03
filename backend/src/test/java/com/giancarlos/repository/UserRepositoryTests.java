package com.giancarlos.repository;

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

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnSavedUser() {
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

        // Act
        User saved = userRepository.save(user);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo("John Doe");
    }

    @Test
    public void UserRepository_FindAll_ReturnMoreThanOneUser() {// Arrange
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

        // Act
        List<User> userList = userRepository.findAll();

        // Assert
        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
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

        // Act
        User found = userRepository.findById(user.getId()).get();

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(user.getId()).isGreaterThan(0L);
        Assertions.assertThat(found.getName()).isEqualTo("John Doe");
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUser() {
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

        // Act
        User found = userRepository.findByEmail(user.getEmail()).get();

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(user.getId()).isGreaterThan(0L);
        Assertions.assertThat(found.getName()).isEqualTo("John Doe");
    }
}
