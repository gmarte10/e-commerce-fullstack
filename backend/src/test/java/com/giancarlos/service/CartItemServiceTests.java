package com.giancarlos.service;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.cartItem.CartItemMapper;
import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.repository.CartItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
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

    @Test
    void CartItemService_GetCartItemByUserId_ReturnMoreThanOneCartItem_Success() {
        Long userId = 1L;
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItems.add(cartItem);

        CartItemDto cartItemDto = new CartItemDto();

        when(cartItemRepository.findByUserId(userId)).thenReturn(cartItems);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        List<CartItemDto> result = cartItemService.getCartItemsByUserId(userId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(cartItemRepository, times(1)).findByUserId(userId);
        verify(cartItemMapper, times(1)).toDto(cartItem);
    }

    @Test
    void CartItemService_CreateCartItem_ReturnCartItem_NewCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setUser(User.builder().id(1L).build());
        cartItem.setProduct(Product.builder().id(2L).build());
        cartItem.setQuantity(1);

        CartItemDto cartItemDto = new CartItemDto();

        when(cartItemRepository.findByUserIdAndProductId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        CartItemDto result = cartItemService.createCartItem(cartItem);

        Assertions.assertThat(result).isNotNull();
        verify(cartItemRepository, times(1)).findByUserIdAndProductId(any(Long.class), any(Long.class));
        verify(cartItemRepository, times(1)).save(cartItem);
        verify(cartItemMapper, times(1)).toDto(cartItem);
    }

    @Test
    void CartItemService_CreateCartItem_ReturnCartItem_ExistingCartItem() {
        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(1L);
        existingCartItem.setQuantity(2);

        CartItem newCartItem = new CartItem();
        newCartItem.setUser(User.builder().id(1L).build());
        newCartItem.setProduct(Product.builder().id(2L).build());
        newCartItem.setQuantity(3);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(1L);
        updatedCartItem.setQuantity(5);

        CartItemDto cartItemDto = new CartItemDto();

        when(cartItemRepository.findByUserIdAndProductId(1L, 2L)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(existingCartItem)).thenReturn(updatedCartItem);
        when(cartItemMapper.toDto(updatedCartItem)).thenReturn(cartItemDto);

        CartItemDto result = cartItemService.createCartItem(newCartItem);

        Assertions.assertThat(result).isNotNull();
        verify(cartItemRepository, times(1)).findByUserIdAndProductId(1L, 2L);
        verify(cartItemRepository, times(1)).save(existingCartItem);
        verify(cartItemMapper, times(1)).toDto(updatedCartItem);
    }

    @Test
    void CartItemService_RemoveCartItemsByUserId_ReturnNothing() {
        Long userId = 1L;

        doNothing().when(cartItemRepository).deleteByUserId(userId);

        cartItemService.removeCartItemsByUserId(userId);

        verify(cartItemRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    void CartItemService_RemoveCartItem_ReturnNothing_DecreaseQuantity() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setUserId(1L);
        cartItemDto.setProductId(2L);
        cartItemDto.setQuantity(2);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);

        when(cartItemRepository.findByUserIdAndProductId(cartItemDto.getUserId(), cartItemDto.getProductId()))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        cartItemService.removeCartItem(cartItemDto);

        Assertions.assertThat(cartItem.getQuantity()).isEqualTo(1);
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void CartItemService_RemoveCartItem_ReturnNothing() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setUserId(1L);
        cartItemDto.setProductId(2L);
        cartItemDto.setQuantity(1);

        CartItem cartItem = new CartItem();

        when(cartItemRepository.findByUserIdAndProductId(cartItemDto.getUserId(), cartItemDto.getProductId()))
                .thenReturn(Optional.of(cartItem));

        doNothing().when(cartItemRepository).delete(cartItem);

        cartItemService.removeCartItem(cartItemDto);

        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void CartItemService_GetCartItemByUserIdAndProductId_ReturnCartItem_Success() {
        Long userId = 1L;
        Long productId = 2L;

        CartItem cartItem = new CartItem();
        CartItemDto cartItemDto = new CartItemDto();

        when(cartItemRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(cartItem));
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        CartItemDto result = cartItemService.getCartItemByUserIdAndProductId(userId, productId);

        Assertions.assertThat(result).isNotNull();
        verify(cartItemRepository, times(1)).findByUserIdAndProductId(userId, productId);
        verify(cartItemMapper, times(1)).toDto(cartItem);
    }

    @Test
    void CartItemService_GetCartItemByUserIdAndProductId_ReturnCartItem_NotFound() {
        Long userId = 1L;
        Long productId = 2L;

        when(cartItemRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class, () -> cartItemService.getCartItemByUserIdAndProductId(userId, productId));

        verify(cartItemRepository, times(1)).findByUserIdAndProductId(userId, productId);
    }
}
