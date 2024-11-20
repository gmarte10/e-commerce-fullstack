package com.giancarlos.mapper;

import com.giancarlos.dto.ProductDto;
import com.giancarlos.dto.ProductResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {
    ProductResponseDto toDTO(ProductDto productDto);
    ProductDto toEntity(ProductResponseDto productResponseDto);
}
