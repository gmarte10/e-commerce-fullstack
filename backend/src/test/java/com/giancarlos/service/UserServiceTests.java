package com.giancarlos.service;

import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import com.giancarlos.repository.UserRepository;
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
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_CreateUser_ReturnUserDto() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$KKnDoi4M5Qrfa861M0EOFe.3sZ18lGAOKaJeaPT5iy1gGSlRV6Kxi")
                .build();

        UserDto userDto = UserDto.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .address("the wall")
                .phone("123-345-6931")
                .build();

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDto);

        UserDto savedUser = userService.register(
                userDto,
                "$2a$12$KKnDoi4M5Qrfa861M0EOFe.3sZ18lGAOKaJeaPT5iy1gGSlRV6Kxi"
        );

        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void UserService_GetAllUsers_ReturnsUserDtoList() {
        // Arrange: Create mock data
        List<User> userList = Arrays.asList(
                User.builder()
                        .name("Jon Snow")
                        .email("jonsnow@gmail.com")
                        .role(UserRole.CUSTOMER)
                        .address("The Wall")
                        .phone("123-345-6931")
                        .build(),
                User.builder()
                        .name("Daenerys Targaryen")
                        .email("dany@gmail.com")
                        .role(UserRole.ADMIN)
                        .address("Dragonstone")
                        .phone("987-654-3210")
                        .build()
        );

        List<UserDto> userDtoList = Arrays.asList(
                UserDto.builder()
                        .name("Jon Snow")
                        .email("jonsnow@gmail.com")
                        .address("The Wall")
                        .phone("123-345-6931")
                        .build(),
                UserDto.builder()
                        .name("Daenerys Targaryen")
                        .email("dany@gmail.com")
                        .address("Dragonstone")
                        .phone("987-654-3210")
                        .build()
        );

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDTO(userList.get(0))).thenReturn(userDtoList.get(0));
        when(userMapper.toDTO(userList.get(1))).thenReturn(userDtoList.get(1));

        List<UserDto> result = userService.getAllUsers();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void UserService_VerifyLogin_ReturnsTokenString() {
        // Arrange
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$KKnDoi4M5Qrfa861M0EOFe.3sZ18lGAOKaJeaPT5iy1gGSlRV6Kxi")
                .build();

        String email = "jonsnow@gmail.com";
        String password = "!targaryen2";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenReturn(authentication);
        String fakeToken = "fake-jwt-token";
        when(jwtService.generateToken(email)).thenReturn(fakeToken);

        // Act
        String token = userService.verify(email, password);

        // Assert
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isEqualTo(fakeToken);
    }

    @Test
    public void UserService_GetUserById_ReturnUserDto() {
        User user = User.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .role(UserRole.CUSTOMER)
                .address("the wall")
                .phone("123-345-6931")
                .password("$2a$12$KKnDoi4M5Qrfa861M0EOFe.3sZ18lGAOKaJeaPT5iy1gGSlRV6Kxi")
                .build();

        UserDto userDto = UserDto.builder()
                .name("Jon Snow")
                .email("jonsnow@gmail.com")
                .address("the wall")
                .phone("123-345-6931")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDto);
        UserDto returnedUser = userService.getUserById(1L);
        Assertions.assertThat(returnedUser).isNotNull();
    }
}
