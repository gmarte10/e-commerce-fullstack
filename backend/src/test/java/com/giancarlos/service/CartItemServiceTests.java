package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.repository.CartItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTests {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartItemService cartItemService;

    private CartItem cartItem;
    private CartItemDto cartItemDto;

    @BeforeEach
    public void init() {
        cartItem = CartItem.builder()
                .user(User.builder().id(1L).build())
                .quantity(5)
                .product(Product.builder().id(1L).build())
                .build();
        cartItemDto = CartItemDto.builder()
                .user(UserDto.builder().id(1L).build())
                .quantity(5)
                .product(ProductDto.builder().id(1L).build())
                .build();
    }

    @Test
    public void CartItemService_GetCartItemById_ReturnCartItem() {
        // Arrange
        Long id = 1L;
        when(cartItemRepository.findById(id)).thenReturn(Optional.ofNullable(cartItem));
        when(cartItemMapper.toDto(Mockito.any(CartItem.class))).thenReturn(cartItemDto);

        // Act
        CartItemDto found = cartItemService.getCartItemById(id);

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found).isEqualTo(cartItemDto);
    }

    @Test
    public void CartItemService_GetCartItemByProductId_ReturnMoreThanOneCartItem() {
        // Arrange
        Long productId = 1L;
        when(cartItemRepository.findByProductId(productId)).thenReturn(List.of(cartItem));
        when(cartItemMapper.toDto(Mockito.any(CartItem.class))).thenReturn(cartItemDto);

        // Act
        List<CartItemDto> cartItemDtoList = cartItemService.getCartItemsByProductId(productId);

        // Assert
        Assertions.assertThat(cartItemDtoList).isNotNull();
        Assertions.assertThat(cartItemDtoList.size()).isEqualTo(1);
        Assertions.assertThat(cartItemDtoList.get(0)).isEqualTo(cartItemDto);
    }

    @Test
    public void CartItemService_GetCartItemByUserId_ReturnMoreThanOneCartItem() {
        // Arrange
        Long productId = 1L;
        when(cartItemRepository.findByUserId(productId)).thenReturn(List.of(cartItem));
        when(cartItemMapper.toDto(Mockito.any(CartItem.class))).thenReturn(cartItemDto);

        // Act
        List<CartItemDto> cartItemDtoList = cartItemService.getCartItemsByUserId(productId);

        // Assert
        Assertions.assertThat(cartItemDtoList).isNotNull();
        Assertions.assertThat(cartItemDtoList.size()).isEqualTo(1);
        Assertions.assertThat(cartItemDtoList.get(0)).isEqualTo(cartItemDto);
    }

    @Test
    public void CartItemService_CreateCartItemExisting_ReturnCartItem() {
        when(cartItemRepository.findByUserIdAndProductId(cartItemDto.getUser().getId(), cartItemDto.getProduct().getId()))
                .thenReturn(Optional.ofNullable(cartItem));
        when(cartItemRepository.save(Mockito.any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(Mockito.any(CartItem.class))).thenReturn(cartItemDto);

        CartItemDto saved = cartItemService.createCartItem(cartItemDto);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getQuantity()).isEqualTo(cartItemDto.getQuantity());
    }

    @Test
    public void CartItemService_CreateCartItemNotExisting_ReturnCartItem() {
        when(cartItemRepository.findByUserIdAndProductId(cartItemDto.getUser().getId(), cartItemDto.getProduct().getId()))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(Mockito.any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(Mockito.any(CartItem.class))).thenReturn(cartItemDto);
        when(cartItemMapper.toEntity(Mockito.any(CartItemDto.class))).thenReturn(cartItem);
        CartItemDto saved = cartItemService.createCartItem(cartItemDto);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getQuantity()).isEqualTo(cartItemDto.getQuantity());
    }

    @Test
    public void CartItemService_GetCartItemsTotalCost_ReturnBigDecimal() {
        // Arrange
        Long userId = 1L;
        BigDecimal total = BigDecimal.valueOf(50);
        when(cartItemRepository.findTotalCostByUserId(userId)).thenReturn(total);

        // Act
        BigDecimal totalCost = cartItemService.getCartItemsTotalCost(userId);

        // Assert
        Assertions.assertThat(totalCost).isNotNull();
        Assertions.assertThat(totalCost).isEqualTo(total);
    }

    @Test
    public void CartItemService_RemoveCartItemsByUserId_ReturnNothing() {
        Long userId = 1L;
        doNothing().when(cartItemRepository).deleteByUserId(userId);
        assertDoesNotThrow(() -> cartItemService.removeCartItemsByUserId(userId));
        verify(cartItemRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    public void CartItemService_RemoveCartItemMoreThanOneQuantity_ReturnNothing() {
        when(cartItemRepository.save(Mockito.any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toEntity(cartItemDto)).thenReturn(cartItem);
        assertDoesNotThrow(() -> cartItemService.removeCartItem(cartItemDto));
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    public void CartItemService_RemoveCartItemOneQuantity_ReturnNothing() {
        cartItemDto.setQuantity(1);
        doNothing().when(cartItemRepository).delete(cartItem);
        when(cartItemMapper.toEntity(cartItemDto)).thenReturn(cartItem);
        assertDoesNotThrow(() -> cartItemService.removeCartItem(cartItemDto));
        verify(cartItemRepository, times(1)).delete(cartItem);
    }
}
