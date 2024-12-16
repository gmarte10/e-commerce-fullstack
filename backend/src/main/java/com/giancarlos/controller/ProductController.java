package com.giancarlos.controller;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.model.Product;
import com.giancarlos.service.ImageService;
import com.giancarlos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        System.out.println("*****************");
        System.out.printf("Received: name=%s, category=%s, description=%s, price=%f, imageFile=%s", name, category, description, price, imageFile);
        Product product = Product.builder()
                .name(name)
                .price(price)
                .category(category)
                .description(description)
                .imageURL(imageService.uploadProductImageToLocal(imageFile))
                .cartItems(new ArrayList<>())
                .orderItems(new ArrayList<>())
                .build();
        ProductDto saved = productService.createProduct(product);
        ProductResponseDto response = productResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ProductResponseDto> createProduct(
//            @RequestPart("name") String name,
//            @RequestPart("category") String category,
//            @RequestPart("description") String description,
//            @RequestPart("price") BigDecimal price,
//            @RequestPart("imageFile") MultipartFile imageFile
//    ) {
//        System.out.println("*****************");
//        System.out.printf("Received: name=%s, category=%s, description=%s, price=%f, imageFile=%s", name, category, description, price, imageFile);
//        Product product = Product.builder()
//                .name(name)
//                .price(price)
//                .category(category)
//                .description(description)
//                .imageURL(imageService.uploadProductImageToLocal(imageFile))
//                .cartItems(new ArrayList<>())
//                .orderItems(new ArrayList<>())
//                .build();
//        ProductDto saved = productService.createProduct(product);
//        ProductResponseDto response = productResponseMapper.toResponse(saved);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeProductById(@PathVariable Long productId) {
        productService.removeProductById(productId);
        return new ResponseEntity<>("Product Successfully Removed", HttpStatus.OK);
    }
}
