package com.giancarlos.controller;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.cartItem.CartItemRequestDto;
import com.giancarlos.dto.cartItem.CartItemResponseDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.cartItem.CartItemRequestMapper;
import com.giancarlos.mapper.cartItem.CartItemResponseMapper;
import com.giancarlos.service.CartItemService;
import com.giancarlos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;
    private final UserService userService;
    private final CartItemResponseMapper cartItemResponseMapper;
    private final CartItemRequestMapper cartItemRequestMapper;

    public CartItemController(CartItemRequestMapper cartItemRequestMapper,
                              CartItemService cartItemService,
                              UserService userService,
                              CartItemResponseMapper cartItemResponseMapper) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.cartItemResponseMapper = cartItemResponseMapper;
        this.cartItemRequestMapper = cartItemRequestMapper;
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<List<CartItemResponseDto>> getCartItemsByUserEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        List<CartItemDto> cartItems = cartItemService.getCartItemsByUserId(userId);
        List<CartItemResponseDto> response = new ArrayList<>();
        for (CartItemDto cartItemDto : cartItems) {
            response.add(cartItemResponseMapper.toResponse(cartItemDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CartItemResponseDto> createCartItem(@RequestBody CartItemRequestDto cartItemRequestDto) {
        CartItemDto saved = cartItemService.createCartItem(cartItemRequestMapper.toDto(cartItemRequestDto));
        CartItemResponseDto response = cartItemResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{email}/{productId}")
    public ResponseEntity<String> removeCartItemByEmailAndProductId(@PathVariable String email, @PathVariable Long productId) {
        UserDto userDto = userService.getUserByEmail(email);
        CartItemDto cartItemDto = cartItemService.getCartItemsByUserIdAndProductId(userDto.getId(), productId);
        cartItemService.removeCartItem(cartItemDto);
        String response = "CartItem Removed";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/clear/{email}")
    public ResponseEntity<String> removeCartItemsByEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        cartItemService.removeCartItemsByUserId(userId);
        String response = "CartItem Removed";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
