package com.giancarlos.mapper.product;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductRequestDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.service.ImageService;
import org.springframework.stereotype.Component;

@Component
public class ProductRequestMapper {
    private final ImageService imageService;

    public ProductRequestMapper(ImageService imageService) {
        this.imageService = imageService;

    }
    public ProductDto toDto(ProductRequestDto productRequestDto) {
        if (productRequestDto == null) {
            throw new ProductNotFoundException("ProductRequestDto is null in ProductRequestMapper in toDto");
        }
        return ProductDto.builder()
                .name(productRequestDto.getName())
                .price(productRequestDto.getPrice())
                .category(productRequestDto.getCategory())
                .description(productRequestDto.getDescription())
                .imageURL(imageService.uploadProductToMemory(productRequestDto.getImageFile()))
                .build();
    }
}
