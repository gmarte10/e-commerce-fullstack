package com.giancarlos.repository;

import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTestsOld {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRepositoryNotNull() {
        Assertions.assertThat(userRepository).isNotNull();
    }

    @Test
    public void UserRepository_saveAll_ReturnSavedUser() {
        // Arrange
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("123").build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$W9Aqtx0dbCUFToiw98pl8./UMSjHOXpV3JsU3hG.m1.umc.XQBxaq")
                .build();

        User user2 = User.builder()
                .name("Daenerys Targaryen")
                .email("danytarg3@outlook.com")
                .role(UserRole.CUSTOMER)
                .address("dragon stone")
                .phone("452-678-46267")
                .password("$2a$12$X1CrKpzqMV.SnKdvJmmzTOz0CsTromv3eKMkJKyaZSGm7qALFrwHu")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnUserNotNull() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$W9Aqtx0dbCUFToiw98pl8./UMSjHOXpV3JsU3hG.m1.umc.XQBxaq")
                .build();

        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).get();

        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserNotNull() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$W9Aqtx0dbCUFToiw98pl8./UMSjHOXpV3JsU3hG.m1.umc.XQBxaq")
                .build();

        userRepository.save(user);

        User foundUser = userRepository.findByEmail(user.getEmail()).get();

        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserRepository_UpdateUser_ReturnUserNotNull() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$W9Aqtx0dbCUFToiw98pl8./UMSjHOXpV3JsU3hG.m1.umc.XQBxaq")
                .build();

        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).get();
        foundUser.setName("Jon Stark");
        foundUser.setPhone("121-325-6951");

        User updatedUser = userRepository.save(foundUser);

        Assertions.assertThat(updatedUser.getName()).isNotNull();
        Assertions.assertThat(updatedUser.getPhone()).isNotNull();
    }

    @Test
    public void UserRepository_UserDeleteById_ReturnUserIsEmpty() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$W9Aqtx0dbCUFToiw98pl8./UMSjHOXpV3JsU3hG.m1.umc.XQBxaq")
                .build();

        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> userReturn = userRepository.findById(user.getId());

        Assertions.assertThat(userReturn).isEmpty();
    }







}
