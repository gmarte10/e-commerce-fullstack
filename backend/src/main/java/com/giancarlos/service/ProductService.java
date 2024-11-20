package com.giancarlos.service;

import com.giancarlos.dto.ProductDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.mapper.ProductMapper;
import com.giancarlos.model.Product;
import com.giancarlos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductDto createProduct(ProductDto productDto) {
        validateProduct(productDto);
        return productMapper.toDTO(productRepository.save(productMapper.toEntity(productDto)));
    }

    public void deleteProduct(String name) {
        // Remember to delete cartItems and maybe orderItems
        Product product = productMapper.toEntity(findByName(name));
        productRepository.delete(product);
    }

    public ProductDto findByName(String name) {
        Optional<Product> found = productRepository.findByName(name);
        if (found.isEmpty()) {
            throw new ProductNotFoundException("Product was not found");
        }
        return productMapper.toDTO(found.get());
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
