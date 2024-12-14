package com.giancarlos.service;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.exception.OrderNotFoundException;
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

    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return convertToProductDtoList(products);
    }

    public List<ProductDto> getProductsBySearch(String search) {
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

    public ProductDto createProduct(Product product) {
        validateProduct(productMapper.toDto(product));
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    public Product getProductById(Long id) {
        Optional<Product> found = productRepository.findById(id);
        if (found.isEmpty()) {
            throw new ProductNotFoundException("Product was not found in ProductService findById");
        }
        return found.get();
    }

    public void removeProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    private void validateProduct(ProductDto productDto) {
        if (productDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }

}
