package com.giancarlos.mapper.product;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.exception.ProductNotFoundException;
import com.giancarlos.service.ImageService;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseMapper {
    private final ImageService imageService;

    public ProductResponseMapper(ImageService imageService) {
        this.imageService = imageService;
    }
    public ProductResponseDto toResponse(ProductDto productDto) {
        if (productDto == null) {
            throw new ProductNotFoundException("ProductDto parameter is null in ProductResponseMapper in toResponse");
        }
        return ProductResponseDto.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .category(productDto.getCategory())
                .imageBase64(imageService.getImageBase64(productDto.getImageURL()))
                .build();
    }
}
