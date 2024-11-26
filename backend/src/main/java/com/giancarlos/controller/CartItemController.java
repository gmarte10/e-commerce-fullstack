package com.giancarlos.controller;

import com.giancarlos.dto.*;
import com.giancarlos.mapper.UserMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.service.CartItemService;
import com.giancarlos.service.ImageService;
import com.giancarlos.service.ProductService;
import com.giancarlos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final ImageService imageService;

    public CartItemController(CartItemService cartItemService, UserService userService, ProductService productService, ImageService imageService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<CartItemAsProductDto>> getAllCartItemsByUserEmailAsProducts(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        List<CartItemDto> cartItems = cartItemService.findByUserId(userId);
        List<CartItemAsProductDto> response = new ArrayList<>();
        for (CartItemDto item : cartItems) {
            ProductDto productDto = productService.findById(item.getProductId());
            String base64 = imageService.getImageBase64(productDto.getImageURL());
            CartItemAsProductDto res = new CartItemAsProductDto();
            res.setProductId(item.getProductId());
            res.setName(productDto.getName());
            res.setCategory(productDto.getCategory());
            res.setDescription(productDto.getDescription());
            res.setImageBase64(base64);
            res.setPrice(productDto.getPrice());
            res.setQuantity(item.getQuantity());
            res.setCartItemId(item.getId());
            response.add(res);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponseDto> addCartItem(@RequestBody CartItemRequestDto cartItemRequestDto) {
        String cirEmail = cartItemRequestDto.getEmail();
        System.out.println("Email: " + cirEmail);
        Long userId = userService.getUserByEmail(cartItemRequestDto.getEmail()).getId();
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(cartItemRequestDto.getQuantity());
        cartItemDto.setUserId(userId);
        cartItemDto.setProductId(cartItemRequestDto.getProductId());
        CartItemDto saved = cartItemService.addCartItem(cartItemDto);
        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        UserDto user = userService.getUserById(saved.getUserId());
        String email = user.getEmail();
        cartItemResponseDto.setEmail(email);
        cartItemResponseDto.setQuantity(saved.getQuantity());
        cartItemResponseDto.setProductId(saved.getProductId());
        return new ResponseEntity<>(cartItemResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{email}/{productId}")
    public ResponseEntity<String> removeCartItem(@PathVariable String email, @PathVariable Long productId) {
        cartItemService.removeProductFromCart(email, productId);
        return new ResponseEntity<>("CartItem was deleted", HttpStatus.OK);
    }

    @DeleteMapping("/clear/{email}")
    public ResponseEntity<String> clearUserCartItems(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        cartItemService.clearUserCartItems(userId);
        return new ResponseEntity<>("CartItems cleared", HttpStatus.OK);
    }

}
