package com.giancarlos.controller;

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
    private final ImageService imageService;

    public ProductController(ProductService productService,
                             ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductDto> products = productService.findAllProducts();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (ProductDto p : products) {
            String base64 = imageService.getImageBase64(p.getImageURL());
            ProductResponseDto prrDto = new ProductResponseDto();
            prrDto.setId(p.getId());
            prrDto.setName(p.getName());
            prrDto.setCategory(p.getCategory());
            prrDto.setDescription(p.getDescription());
            prrDto.setImageBase64(base64);
            prrDto.setPrice(p.getPrice());
            productResponseDtos.add(prrDto);
        }
        return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDto> createProduct(@ModelAttribute ProductRequestDto productRequestDto) {
        ProductDto productDto = new ProductDto();
        String fileName = imageService.uploadProductToMemory(productRequestDto.getImageFile());
        productDto.setName(productRequestDto.getName());
        productDto.setCategory(productRequestDto.getCategory());
        productDto.setDescription(productRequestDto.getDescription());
        productDto.setImageURL(fileName);

        ProductDto savedProduct = productService.createProduct(productDto);

        ProductResponseDto productResponseDto = new ProductResponseDto();
        String base64 = imageService.getImageBase64(savedProduct.getImageURL());
        productResponseDto.setName(savedProduct.getName());
        productResponseDto.setCategory(savedProduct.getCategory());
        productResponseDto.setDescription(savedProduct.getDescription());
        productResponseDto.setImageBase64(base64);
        productResponseDto.setPrice(savedProduct.getPrice());

        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }
}
