package com.giancarlos.controller;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.mapper.product.ProductRequestMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ProductResponseMapper productResponseMapper;
    private final ProductRequestMapper productRequestMapper;

    public ProductController(ProductService productService,
                             ProductResponseMapper productResponseMapper,
                             ProductRequestMapper productRequestMapper) {
        this.productService = productService;
        this.productRequestMapper = productRequestMapper;
        this.productResponseMapper = productResponseMapper;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        List<ProductDto> products = productService.findAllProducts();
        List<ProductResponseDto> response = new ArrayList<>();
        for (ProductDto productDto : products) {
            response.add(productResponseMapper.toResponse(productDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(@ModelAttribute ProductRequestDto productRequestDto) {
        ProductDto saved = productService.createProduct(productRequestMapper.toDto(productRequestDto));
        ProductResponseDto response = productResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
