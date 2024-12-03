package com.giancarlos.service;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import com.giancarlos.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_Register_ReturnUserDto() {

        // Arrange
        String password = "temp";
        User user = User.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password(password)
                .orders(null)
                .cartItems(null)
                .build();
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email("johndoe@gmail.com")
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .orders(null)
                .cartItems(null)
                .build();

        when(userMapper.toEntity(Mockito.any(UserDto.class))).thenReturn(user);
        when(userMapper.toDto(Mockito.any(User.class))).thenReturn(userDto);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Act
        UserDto saved = userService.register(userDto, password);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo("John Doe");
    }

    @Test
    public void UserService_GetUserByEmail_ReturnUserDto() {
        // Arrange
        String email = "johndoe@gmail.com";
        User user = User.builder()
                .name("John Doe")
                .email(email)
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password("temp")
                .orders(null)
                .cartItems(null)
                .build();
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email(email)
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .orders(null)
                .cartItems(null)
                .build();

        when(userMapper.toDto(Mockito.any(User.class))).thenReturn(userDto);
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        // Act
        UserDto found = userService.getUserByEmail(email);

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("John Doe");
    }

    @Test
    public void UserService_Verify_ReturnString() {
        // Arrange
        String email = "johndoe@gmail.com";
        String password = "temp";
        User user = User.builder()
                .name("John Doe")
                .email(email)
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password(password)
                .orders(null)
                .cartItems(null)
                .build();
        String token = "token";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(Mockito.any(String.class))).thenReturn(token);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        // Act
        String returnToken = userService.verify(email, password);

        // Assert
        Assertions.assertThat(returnToken).isNotNull();
        Assertions.assertThat(returnToken).isEqualTo(token);
    }

    @Test
    public void UserService_GetUserRole_ReturnUserRole() {
        // Arrange
        String email = "johndoe@gmail.com";
        String password = "temp";
        User user = User.builder()
                .name("John Doe")
                .email(email)
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password(password)
                .orders(null)
                .cartItems(null)
                .build();
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.ofNullable(user));

        // Act
        UserRole userRole = userService.getUserRole(email);

        // Assert
        Assertions.assertThat(userRole).isNotNull();
        Assertions.assertThat(userRole.toString()).isEqualTo("CUSTOMER");
    }

    @Test
    public void UserService_GetUsers_ReturnMoreThanOneUserDto() {
        // Arrange
        String email = "johndoe@gmail.com";
        String email2 = "janedoe@gmail.com";
        String password = "temp";
        String password2 = "pass";
        User user = User.builder()
                .name("John Doe")
                .email(email)
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .password(password)
                .orders(null)
                .cartItems(null)
                .build();
        UserDto userDto = UserDto.builder()
                .name("John Doe")
                .email(email)
                .phone("123-456-7891")
                .address("123 Sesame St")
                .role(UserRole.CUSTOMER)
                .orders(null)
                .cartItems(null)
                .build();
        User user2 = User.builder()
                .name("Jane Doe")
                .email(email2)
                .phone("789-456-7891")
                .address("543 Sesame St")
                .role(UserRole.ADMIN)
                .password(password2)
                .orders(null)
                .cartItems(null)
                .build();
        UserDto userDto2 = UserDto.builder()
                .name("Jane Doe")
                .email(email2)
                .phone("789-456-7891")
                .address("543 Sesame St")
                .role(UserRole.ADMIN)
                .orders(null)
                .cartItems(null)
                .build();
        List<User> userList = List.of(user, user2);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(userMapper.toDto(user2)).thenReturn(userDto2);

        // Act
        List<UserDto> userDtoList = userService.getUsers();

        // Assert
        Assertions.assertThat(userDtoList).isNotNull();
        Assertions.assertThat(userDtoList.size()).isEqualTo(2);

        UserDto dto1 = userDtoList.get(0);
        UserDto dto2 = userDtoList.get(1);

        Assertions.assertThat(dto1.getName()).isEqualTo("John Doe");
        Assertions.assertThat(dto1.getEmail()).isEqualTo(email);
        Assertions.assertThat(dto1.getRole()).isEqualTo(UserRole.CUSTOMER);

        Assertions.assertThat(dto2.getName()).isEqualTo("Jane Doe");
        Assertions.assertThat(dto2.getEmail()).isEqualTo(email2);
        Assertions.assertThat(dto2.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    public void UserService_GetUserById_ReturnUserDto() {
        // Arrange
        String email = "johndoe@gmail.com";
        String password = "temp";
        String name = "John Doe";
        String address = "123 Sesame St";
        String phone = "123-456-7891";
        Long id = 1L;
        User user = User.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .role(UserRole.CUSTOMER)
                .password(password)
                .orders(null)
                .cartItems(null)
                .build();
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .role(UserRole.CUSTOMER)
                .orders(null)
                .cartItems(null)
                .build();
        when(userMapper.toDto(Mockito.any(User.class))).thenReturn(userDto);
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        // Act
        UserDto found = userService.getUserById(id);

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo(name);
    }
}
