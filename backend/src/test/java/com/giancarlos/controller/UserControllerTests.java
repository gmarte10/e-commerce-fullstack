package com.giancarlos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.model.UserRole;
import com.giancarlos.service.JwtService;
import com.giancarlos.service.UserDetailService;
import com.giancarlos.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private UserDetailService userDetailService;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @MockBean
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setup() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    @Test
//    void whenValidRegistration_thenReturns201() throws Exception {
//        // Arrange
//        UserDto userDto = UserDto.builder()
//                .name("John Doe")
//                .email("john@example.com")
//                .address("123 Main St")
//                .phone("1234567890")
//                .role(UserRole.CUSTOMER)
//                .build();
//
//        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
//                .userDto(userDto)
//                .password("password123")
//                .build();
//
//        String encodedPassword = "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG";
//
//        when(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
//                .thenReturn(encodedPassword);
//
//        when(userService.register(registerRequest.getUserDto(), encodedPassword))
//                .thenReturn(userDto);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value(userDto.getName()))
//                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
//                .andExpect(jsonPath("$.address").value(userDto.getAddress()))
//                .andExpect(jsonPath("$.phone").value(userDto.getPhone()));
//
//        verify(userService).register(any(UserDto.class), eq(encodedPassword));
//    }
//}