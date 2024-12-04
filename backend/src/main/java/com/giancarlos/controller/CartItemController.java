package com.giancarlos.controller;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.cartItem.CartItemRequestDto;
import com.giancarlos.dto.cartItem.CartItemResponseDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.cartItem.CartItemRequestMapper;
import com.giancarlos.mapper.cartItem.CartItemResponseMapper;
import com.giancarlos.service.CartItemService;
import com.giancarlos.service.ProductService;
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
    private final ProductService productService;
    private final CartItemResponseMapper cartItemResponseMapper;
    private final CartItemRequestMapper cartItemRequestMapper;

    public CartItemController(CartItemRequestMapper cartItemRequestMapper,
                              CartItemService cartItemService,
                              UserService userService,
                              ProductService productService,
                              CartItemResponseMapper cartItemResponseMapper) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
        this.cartItemResponseMapper = cartItemResponseMapper;
        this.cartItemRequestMapper = cartItemRequestMapper;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<CartItemResponseDto>> getCartItemsByUserEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        List<CartItemDto> cartItems = cartItemService.getCartItemsByUserId(userId);
        List<CartItemResponseDto> response = new ArrayList<>();
        for (CartItemDto cartItemDto : cartItems) {
            response.add(cartItemResponseMapper.toResponse(cartItemDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponseDto> addCartItem(@RequestBody CartItemRequestDto cartItemRequestDto) {
        CartItemDto saved = cartItemService.createCartItem(cartItemRequestMapper.toDto(cartItemRequestDto));
        CartItemResponseDto response = cartItemResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{email}/{productId}")
    public ResponseEntity<String> removeCartItemByEmailAndProductId(@PathVariable String email, @PathVariable Long productId) {
        UserDto userDto = userService.getUserByEmail(email);
        ProductDto productDto = productService.getProductById(productId);
        String response = removeCartItemResponse(email, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/clear/{email}")
    public ResponseEntity<String> removeCartItemsByEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        cartItemService.removeCartItemsByUserId(userId);
        String response = removeCartItemResponse(email, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String removeCartItemResponse(String email, Long productId) {
        String intro = "CartItem(s) Successfully Deleted From {email}: ";
        if (productId != null) {
            ProductDto productDto = productService.getProductById(productId);
            return intro + String.format("Product Name: %s", productDto.getName());
        }
        else {
            return intro + "All CartItems";
        }
    }

}
