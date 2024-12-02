package com.giancarlos.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {
    ProductResponseDto toDTO(ProductDto productDto);
    ProductDto toEntity(ProductResponseDto productResponseDto);
}
