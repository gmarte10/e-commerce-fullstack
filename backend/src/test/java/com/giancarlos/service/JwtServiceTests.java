package com.giancarlos.service;

import io.jsonwebtoken.Jwt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void init() {

    }

    @Test
    public void JwtService_GenerateToken_ReturnString() {
        // Arrange
        String username = "johndoe@gmail.com";

        // Act
        String token = jwtService.generateToken(username);

        // Assert
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token.length()).isGreaterThan(0);
    }

    @Test
    public void JwtService_ExtractUsername_ReturnString() {
        // Arrange
        String username = "johndoe@gmail.com";
        String token = jwtService.generateToken(username);

        // Act
        String extractedUsername = jwtService.extractUserName(token);

        // Assert
        Assertions.assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    public void JwtService_ValidateToken_ReturnTrue() {
        String username = "johndoe@gmail.com";
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.generateToken(username);
        Boolean isValid = jwtService.validateToken(token, userDetails);
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void JwtService_ValidateToken_ReturnFalse_InvalidUsername() {
        String username = "johndoe@gmail.com";
        when(userDetails.getUsername()).thenReturn("janedoe@gmail.com");
        String token = jwtService.generateToken(username);
        Boolean isValid = jwtService.validateToken(token, userDetails);
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void JwtService_ValidateToken_ReturnFalse_Expired() throws InterruptedException {
        String username = "johndoe@gmail.com";
        when(userDetails.getUsername()).thenReturn(username);
        String token = jwtService.generateTokenTesting(username, 100);
        Thread.sleep(200);
        Boolean isValid = jwtService.validateToken(token, userDetails);
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void JwtService_ExtractUserName_ThrowException_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> jwtService.extractUserName(invalidToken));
    }
}
