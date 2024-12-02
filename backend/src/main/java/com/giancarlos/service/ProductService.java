package com.giancarlos.service;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.model.Product;
import com.giancarlos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return convertToProductDtoList(products);
    }

    public List<ProductDto> findByPriceLessThanEqual(BigDecimal price) {
        List<Product> products = productRepository.findByPriceLessThanEqual(price);
        return convertToProductDtoList(products);
    }

    public List<ProductDto> findByCategoriesName(String categoryName) {
        List<Product> products = productRepository.findByCategory(categoryName);
        return convertToProductDtoList(products);
    }

    public List<ProductDto> search(String search) {
        List<Product> products = productRepository.searchByNameAndCategory(search);
        return convertToProductDtoList(products);
    }

    private List<ProductDto> convertToProductDtoList(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(productMapper.toDto(product));
        }
        return productDtos;
    }

    public ProductDto createProduct(ProductDto productDto) {
        validateProduct(productDto);
        Product product = productRepository.save(productMapper.toEntity(productDto));
        return productMapper.toDto(product);
    }

    // Testing: delete all cartItems and orderItems that contain the product
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public ProductDto findById(Long id) {
        Optional<Product> found = productRepository.findById(id);
        if (found.isEmpty()) {
            throw new ProductNotFoundException("Product was not found in ProductService findById");
        }
        return productMapper.toDto(found.get());
    }

    private void validateProduct(ProductDto productDto) {
        if (productDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }

}
