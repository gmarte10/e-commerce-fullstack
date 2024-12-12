package com.giancarlos.service;


import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.model.Product;
import com.giancarlos.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void ProductService_GetProducts_ReturnMoreThanOneProductDto_Success() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);

        ProductDto productDto = new ProductDto();

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getProducts();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void ProductService_GetProductsBySearch_ReturnMoreThanOneProductDto_Success() {
        String search = "example";
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);

        ProductDto productDto = new ProductDto();

        when(productRepository.searchByNameAndCategory(search)).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getProductsBySearch(search);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(productRepository, times(1)).searchByNameAndCategory(search);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void ProductService_CreateProduct_ReturnProductDto_Success() {
        Product product = new Product();
        Product savedProduct = Product.builder()
                .id(1L)
                .name("laptop")
                .price(BigDecimal.valueOf(3))
                .category("tech")
                .imageURL("laptop.jpg")
                .description("fast and powerful laptop")
                .cartItems(null)
                .orderItems(null)
                .build();
        ProductDto productDto = ProductDto.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(3))
                .category("tech")
                .imageURL("laptop.jpg")
                .description("fast and powerful laptop")
                .cartItems(null)
                .orderItems(null)
                .build();

        when(productMapper.toDto(product)).thenReturn(productDto);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(productDto);

        ProductDto result = productService.createProduct(product);

        Assertions.assertThat(result).isNotNull();
        verify(productMapper, times(1)).toDto(product);
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).toDto(savedProduct);
    }

    @Test
    void ProductService_getProductById_ReturnProductDtoDto_Success() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(productId);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void ProductService_getProductById_ReturnProductDtoDto_NotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void ProductService_CreateProduct_ReturnProductDto_NegativePrice() {
        ProductDto productDto = new ProductDto();
        productDto.setPrice(new BigDecimal("-10.00"));

        Product product = new Product();

        when(productMapper.toDto(product)).thenReturn(productDto);

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
        verify(productMapper, times(1)).toDto(product);
    }
}
