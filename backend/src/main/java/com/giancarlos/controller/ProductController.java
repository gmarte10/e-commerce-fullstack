package com.giancarlos.controller;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.model.Product;
import com.giancarlos.service.ImageService;
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
    private final ImageService imageService;

    public ProductController(ProductService productService,
                             ProductResponseMapper productResponseMapper,
                             ImageService imageService) {
        this.productService = productService;
        this.productResponseMapper = productResponseMapper;
        this.imageService = imageService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponseDto>> getProducts() {
        List<ProductDto> products = productService.getProducts();
        List<ProductResponseDto> response = new ArrayList<>();
        for (ProductDto productDto : products) {
            response.add(productResponseMapper.toResponse(productDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<ProductResponseDto>> searchForProduct(@PathVariable String search) {
        List<ProductDto> products = productService.getProductsBySearch(search);
        List<ProductResponseDto> response = new ArrayList<>();
        for (ProductDto productDto : products) {
            response.add(productResponseMapper.toResponse(productDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .price(productRequestDto.getPrice())
                .category(productRequestDto.getCategory())
                .description(productRequestDto.getDescription())
                .imageURL(imageService.uploadProductImageToLocal(productRequestDto.getImageFile()))
                .cartItems(new ArrayList<>())
                .orderItems(new ArrayList<>())
                .build();
        ProductDto saved = productService.createProduct(product);
        ProductResponseDto response = productResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
