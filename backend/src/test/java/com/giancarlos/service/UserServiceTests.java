package com.giancarlos.service;

import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void UserService_Register_ReturnUserDto_Success() {
        User user = new User();
        User savedUser = new User();
        UserDto userDto = new UserDto();

        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userDto);

        UserDto result = userService.register(user);

        Assertions.assertThat(result).isNotNull();
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(savedUser);
    }

    @Test
    void UserService_GetUserByEmail_ReturnUserDto_Success() {
        String email = "test@example.com";
        User user = new User();
        UserDto userDto = new UserDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByEmail(email);

        Assertions.assertThat(result).isNotNull();
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void UserService_GetUserByEmail_ReturnUserDto_NotFound() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void UserService_Verify_ReturnString_Success() {
        String email = "test@example.com";
        String rawPassword = "password";
        User user = new User();
        Authentication authentication = mock(Authentication.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(email)).thenReturn("mockedToken");

        String result = userService.verify(email, rawPassword);

        Assertions.assertThat(result).isEqualTo("mockedToken");
        verify(userRepository, times(1)).findByEmail(email);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(email);
    }

    @Test
    void UserService_Verify_ReturnString_UserNotFound() {
        String email = "notfound@example.com";
        String rawPassword = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        String result = userService.verify(email, rawPassword);

        Assertions.assertThat(result).isEqualTo("Failed to authenticate. User email does not exist");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void UserService_Verify_ReturnString_InvalidPassword() {
        String email = "test@example.com";
        String rawPassword = "wrongpassword";
        User user = new User();
        Authentication authentication = mock(Authentication.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        String result = userService.verify(email, rawPassword);

        Assertions.assertThat(result).isEqualTo("Failed to authenticate. User password is incorrect");
        verify(userRepository, times(1)).findByEmail(email);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void UserService_GetUserById_ReturnUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(userId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void UserService_GetUserById_ReturnUser_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }
}
