package com.giancarlos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.cartItem.CartItemRequestDto;
import com.giancarlos.dto.cartItem.CartItemResponseDto;
import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderRequestDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.cartItem.CartItemRequestMapper;
import com.giancarlos.mapper.cartItem.CartItemResponseMapper;
import com.giancarlos.mapper.order.OrderRequestMapper;
import com.giancarlos.mapper.order.OrderResponseMapper;
import com.giancarlos.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartItemRequestMapper cartItemRequestMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CartItemResponseMapper cartItemResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void CartItemController_GetCartItemsByUserEmail_ReturnMoreThanOneCartItemResponse() throws Exception {
        CartItemDto cartItemDto = CartItemDto.builder().id(2L).build();
        CartItemResponseDto cartItemResponseDto = CartItemResponseDto.builder().id(2L).build();
        List<CartItemDto> cartItemDtoList = List.of(cartItemDto);
        String email = "test@email.com";
        Long userId = 1L;
        UserDto userDto = UserDto.builder().id(userId).build();
        when(userService.getUserByEmail(email)).thenReturn(userDto);
        when(cartItemResponseMapper.toResponse(cartItemDto)).thenReturn(cartItemResponseDto);
        when(cartItemService.getCartItemsByUserId(userId)).thenReturn(cartItemDtoList);
        ResultActions resultActions = mockMvc.perform(get("/api/cart-items/get/{email}", email)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
        verify(userService, times(1)).getUserByEmail("test@email.com");
        verify(cartItemService, times(1)).getCartItemsByUserId(userId);
        verify(cartItemResponseMapper, times(1)).toResponse(cartItemDto);
    }

    @Test
    public void CartItemController_CreateCartItem_ReturnCartItemResponse() throws Exception {
        String email = "test@email.com";
        CartItemRequestDto cartItemRequestDto = CartItemRequestDto.builder().email(email).build();
        CartItemDto cartItemDto = CartItemDto.builder().id(1L).build();
        CartItemResponseDto cartItemResponseDto = CartItemResponseDto.builder().id(1L).build();
        when(cartItemService.createCartItem(any(CartItemDto.class))).thenReturn(cartItemDto);
        when(cartItemRequestMapper.toDto(cartItemRequestDto)).thenReturn(cartItemDto);
        when(cartItemResponseMapper.toResponse(cartItemDto)).thenReturn(cartItemResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/api/cart-items/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemRequestDto)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(cartItemService, times(1)).createCartItem(cartItemDto);
        verify(cartItemResponseMapper, times(1)).toResponse(cartItemDto);
        verify(cartItemRequestMapper, times(1)).toDto(cartItemRequestDto);
    }

    @Test
    public void CartItemController_RemoveCartItemByEmailAndProductId_ReturnString() throws Exception {
        String email = "test@email.com";
        UserDto userDto = UserDto.builder().id(1L).email(email).build();
        CartItemDto cartItemDto = CartItemDto.builder().id(2L).build();

        when(userService.getUserByEmail(email)).thenReturn(userDto);
        when(cartItemService.getCartItemsByUserIdAndProductId(1L, 2L)).thenReturn(cartItemDto);

        mockMvc.perform(delete("/api/cart-items/remove/{email}/{productId}", email, 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem Removed"));

        verify(userService, times(1)).getUserByEmail(email);
        verify(cartItemService, times(1)).getCartItemsByUserIdAndProductId(1L, 2L);


    }

    @Test
    public void CartItemController_RemoveCartItemsByEmail_ReturnString() throws Exception {
        // Mock input
        UserDto userDto = UserDto.builder().id(1L).email("test@gmail.com").build();

        when(userService.getUserByEmail("test@gmail.com")).thenReturn(userDto);

        // Perform request
        mockMvc.perform(delete("/api/cart-items/clear/{email}", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("CartItem Removed"));

        // Verify interactions
        verify(userService, times(1)).getUserByEmail("test@gmail.com");
        verify(cartItemService, times(1)).removeCartItemsByUserId(1L);
    }
}
