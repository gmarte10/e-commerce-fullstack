package com.giancarlos.controller;

import com.giancarlos.dto.CartItemDto;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.service.CartItemService;
import com.giancarlos.service.ProductService;
import com.giancarlos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<CartItemDto>> getAllCartItemsByUserEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        List<CartItemDto> cartItemDtos = cartItemService.findByUserId(userId);
        return new ResponseEntity<>(cartItemDtos, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemDto> addCartItem(@RequestBody CartItemDto cartItemDto) {
        CartItemDto savedCartItemDto = cartItemService.addCartItem(cartItemDto);
        return new ResponseEntity<>(savedCartItemDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCartItem(@RequestBody CartItemDto cartItemDto) {
        cartItemService.removeProductFromCart(cartItemDto);
        return new ResponseEntity<>("CartItem was deleted", HttpStatus.OK);
    }

    @DeleteMapping("/clear/{email}")
    public ResponseEntity<String> clearUserCartItems(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        cartItemService.clearUserCartItems(userId);
        return new ResponseEntity<>("CartItems cleared", HttpStatus.OK);
    }

}
