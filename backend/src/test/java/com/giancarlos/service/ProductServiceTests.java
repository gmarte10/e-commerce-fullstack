package com.giancarlos.service;


import com.giancarlos.dto.product.ProductDto;
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
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Product product2;
    private ProductDto productDto;
    private ProductDto productDto2;

    @BeforeEach
    public void init() {
        product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productDto = ProductDto.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        product2 = Product.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productDto2 = ProductDto.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();

    }

    @Test
    public void ProductService_DeleteProductById_ReturnNothing() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));
        assertAll(() -> productService.deleteProductById(id));
    }

    @Test
    public void ProductService_GetProducts_ReturnMoreThanOneProductDto() {
        List<Product> productList = List.of(product);
        List<ProductDto> productDtoListExpected = List.of(productDto);
        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDto);

        List<ProductDto> productDtoList = productService.getProducts();
        Assertions.assertThat(productDtoList).isNotNull();
        Assertions.assertThat(productDtoList.size()).isEqualTo(1);
        Assertions.assertThat(productDtoList.get(0)).isEqualTo(productDtoListExpected.get(0));
    }

    @Test
    public void ProductService_GeProductsByPriceLessThanEqual_ReturnMoreThanOneProductDto() {
        BigDecimal price = BigDecimal.valueOf(100);
        when(productRepository.findByPriceLessThanEqual(price)).thenReturn(List.of(product));
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDto);

        List<ProductDto> productDtoList = productService.getProductsByPriceLessThanEqual(price);
        Assertions.assertThat(productDtoList).isNotNull();
        Assertions.assertThat(productDtoList.size()).isEqualTo(1);
        Assertions.assertThat(productDtoList.get(0)).isEqualTo(productDto);
    }

    @Test
    public void ProductService_GetProductsByCategory_ReturnMoreThanOneProductDto() {
        String category = "technology";
        when(productRepository.findByCategory(category)).thenReturn(List.of(product2));
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDto2);

        List<ProductDto> productDtoList = productService.getProductsByCategory(category);
        Assertions.assertThat(productDtoList).isNotNull();
        Assertions.assertThat(productDtoList.size()).isEqualTo(1);
        Assertions.assertThat(productDtoList.get(0)).isEqualTo(productDto2);
    }

    @Test
    public void ProductService_GetProductsBySearch_ReturnMoreThanOneProductDto() {
        String search = "tech";
        when(productRepository.searchByNameAndCategory(search)).thenReturn(List.of(product2));
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDto2);

        List<ProductDto> productDtoList = productService.getProductsBySearch(search);
        Assertions.assertThat(productDtoList).isNotNull();
        Assertions.assertThat(productDtoList.size()).isEqualTo(1);
        Assertions.assertThat(productDtoList.get(0)).isEqualTo(productDto2);
    }

    @Test
    public void ProductService_CreateProduct_ReturnProductDto() {
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDto);
        when(productMapper.toEntity(Mockito.any(ProductDto.class))).thenReturn(product);

        ProductDto saved = productService.createProduct(productDto);
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo("hoodie");
    }

    @Test
    public void ProductService_getProductById_ReturnProductDto() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));
        when(productMapper.toDto(Mockito.any(Product.class))).thenReturn(productDto);

        ProductDto found = productService.getProductById(id);
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("hoodie");
    }
}
