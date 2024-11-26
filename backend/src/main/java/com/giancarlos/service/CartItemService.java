package com.giancarlos.service;

import com.giancarlos.dto.CartItemDto;
import com.giancarlos.dto.UserDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.ProductMapper;
import com.giancarlos.mapper.UserMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private ProductService productService;
    private final UserService userService;
    private UserMapper userMapper;
    private ProductMapper productMapper;

    public CartItemService(CartItemRepository cartItemRepository,
                           ProductService productService,
                           UserService userService,
                           UserMapper userMapper,
                           ProductMapper productMapper) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    public CartItemDto findById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new CartItemNotFoundException("Cart Item was not found"));
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setUserId(cartItem.getUser().getId());
        cartItemDto.setProductId(cartItem.getProduct().getId());
        return cartItemDto;
    }

    public List<CartItemDto> findByUserId(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem c : cartItems) {
            Long pId = c.getProduct().getId();
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setId(c.getId());
            cartItemDto.setUserId(userId);
            cartItemDto.setProductId(pId);
            cartItemDto.setQuantity(c.getQuantity());
            cartItemDtos.add(cartItemDto);
        }
        return cartItemDtos;
    }

    public CartItemDto addCartItem(CartItemDto cartItemDto) {
        // If the user already has this product in their cart, increase the quantity appropriately
        User user = userMapper.toEntity(userService.getUserById(cartItemDto.getUserId()));
        if (cartItemRepository.existsByUserId(user.getId())) {
            Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(user.getId(), cartItemDto.getProductId());
            if (existing.isPresent()) {
                int quantity = existing.get().getQuantity() + cartItemDto.getQuantity();
                existing.get().setQuantity(quantity);
                CartItem saved = cartItemRepository.save(existing.get());
                CartItemDto ciDto = new CartItemDto();
                ciDto.setProductId(saved.getProduct().getId());
                ciDto.setUserId(saved.getUser().getId());
                ciDto.setQuantity(saved.getQuantity());
                ciDto.setId(saved.getId());
                return ciDto;
            }
        }
        CartItem cartItem = new CartItem();
        Product product = productMapper.toEntity(productService.findById(cartItemDto.getProductId()));
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        CartItemDto ciDto = new CartItemDto();
        ciDto.setProductId(savedCartItem.getProduct().getId());
        ciDto.setUserId(savedCartItem.getUser().getId());
        ciDto.setQuantity(savedCartItem.getQuantity());
        ciDto.setId(savedCartItem.getId());
        return ciDto;
    }

    public BigDecimal getCartTotal(Long userId) {
        return cartItemRepository.getCartTotal(userId);
    }

    @Transactional
    public void clearUserCartItems(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    @Transactional
    public void removeProductFromCart(String email, Long productId) {
        UserDto user = userService.getUserByEmail(email);
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), productId);
        if (cartItem.isEmpty()) {
            throw new CartItemNotFoundException("Cart item was not found");
        }
        if (cartItem.get().getQuantity() > 1) {
            cartItem.get().setQuantity(cartItem.get().getQuantity() - 1);
            cartItemRepository.save(cartItem.get());
        }
        else {
            cartItemRepository.delete(cartItem.get());
        }
    }
}
