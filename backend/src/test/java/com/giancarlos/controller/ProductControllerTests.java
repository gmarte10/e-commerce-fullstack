package com.giancarlos.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.mapper.product.ProductRequestMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.giancarlos.mapper.user.UserRegisterMapper;
import com.giancarlos.mapper.user.UserResponseMapper;
import com.giancarlos.service.JwtService;
import com.giancarlos.service.ProductService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRequestMapper productRequestMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ProductResponseMapper productResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void ProductController_GetProducts_ReturnMoreThanOneProductResponse() throws Exception {
        ProductDto productDto = ProductDto.builder().id(1L).name("hoodie").build();
        List<ProductDto> productDtoList = List.of(productDto);
        ProductResponseDto productResponseDto = ProductResponseDto.builder().id(1L).name("hoodie").build();
        when(productService.getProducts()).thenReturn(productDtoList);
        when(productResponseMapper.toResponse(productDto)).thenReturn(productResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/products/get-all")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("hoodie"));
        verify(productService, times(1)).getProducts();
        verify(productResponseMapper, times(1)).toResponse(productDto);
    }


    @Test
    public void ProductController_CreateProduct_ReturnProductResponse() throws Exception {
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(32))
                .build();

        ProductDto productDto = ProductDto.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(32))
                .build();

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(32))
                .build();

        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);
        when(productRequestMapper.toDto(productRequestDto)).thenReturn(productDto);
        when(productResponseMapper.toResponse(productDto)).thenReturn(productResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/api/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequestDto)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("hoodie"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(32)));
    }
}