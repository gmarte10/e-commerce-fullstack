package com.giancarlos.service;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.ProductNotFoundException;
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
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product p : products) {
            productDtos.add(productMapper.toDTO(p));
        }
        return productDtos;
    }

    public List<ProductDto> findByPriceLessThanEqual(BigDecimal price) {
        List<Product> products = productRepository.findByPriceLessThanEqual(price);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product p : products) {
            productDtos.add(productMapper.toDTO(p));
        }
        return productDtos;
    }

    public List<ProductDto> findByCategoriesName(String categoryName) {
        List<Product> products = productRepository.findByCategory(categoryName);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product p : products) {
            productDtos.add(productMapper.toDTO(p));
        }
        return productDtos;
    }

    public List<ProductDto> search(String search) {
        List<Product> products = productRepository.searchByNameAndCategory(search);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product p : products) {
            productDtos.add(productMapper.toDTO(p));
        }
        return productDtos;
    }

    // Testing: check if productDto correctly converts to product
    public ProductDto createProduct(ProductDto productDto) {
        validateProduct(productDto);
        Product product = productRepository.save(productMapper.toEntity(productDto));
        return productMapper.toDTO(product);
    }

    // Testing: delete all cartItems and orderItems that contain the product
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public ProductDto findById(Long id) {
        Optional<Product> found = productRepository.findById(id);
        if (found.isEmpty()) {
            throw new ProductNotFoundException("Product was not found");
        }
        return productMapper.toDTO(found.get());
    }

    private void validateProduct(ProductDto productDto) {
        if (productDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }

}
