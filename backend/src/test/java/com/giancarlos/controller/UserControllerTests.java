package com.giancarlos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.dto.user.UserLoginDto;
import com.giancarlos.dto.user.UserRegisterDto;
import com.giancarlos.dto.user.UserResponseDto;
import com.giancarlos.mapper.user.UserRegisterMapper;
import com.giancarlos.mapper.user.UserResponseMapper;

import com.giancarlos.model.UserRole;
import com.giancarlos.service.JwtService;
import com.giancarlos.service.UserService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import javax.xml.transform.Result;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserRegisterMapper userRegisterMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserResponseMapper userResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void UserController_Register_ReturnUserResponseDto() throws Exception {
        // Arrange
        String email = "johndoe@gmail.com";
        String pass = "pass";
        String address = "123 Sesame St";
        UserRegisterDto userRegisterDto = UserRegisterDto.builder()
                .email(email)
                .password(pass)
                .address(address)
                .build();
        UserDto userDto = UserDto.builder()
                .email(email)
                .address(address)
                .role(UserRole.CUSTOMER)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .email(email)
                .address(address)
                .id(1L)
                .role("CUSTOMER")
                .build();

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRegisterMapper.toDto(any(UserRegisterDto.class))).thenReturn(userDto);
        when(userResponseMapper.toResponse(any(UserDto.class))).thenReturn(userResponseDto);
        when(userService.register(any(UserDto.class), anyString())).thenReturn(userDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegisterDto)));

        // Assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    public void UserController_Login_ReturnString() throws Exception {
        UserLoginDto userLoginDto = UserLoginDto.builder()
                .email("email@e.com")
                .password("pass")
                .build();

        String token = "token";

        when(userService.verify(any(String.class), any(String.class))).thenReturn(token);
        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto)));

        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(token));

        verify(userService, times(1)).verify(eq("email@e.com"), eq("pass"));
    }

    @Test
    public void UserController_GetUserInfo_ReturnUserResponseDto() throws Exception {
        String email = "user@email.com";
        String address = "123 Sesame St";
        String name = "John Doe";
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .address(address)
                .role(UserRole.CUSTOMER)
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .name(name)
                .email(email)
                .address(address)
                .id(1L)
                .role("CUSTOMER")
                .build();

        when(userService.getUserByEmail(email)).thenReturn(userDto);
        when(userResponseMapper.toResponse(userDto)).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/users/info/{email}", email)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.address").value(address));

        verify(userService, times(1)).getUserByEmail(email);
        verify(userResponseMapper, times(1)).toResponse(userDto);
    }
}