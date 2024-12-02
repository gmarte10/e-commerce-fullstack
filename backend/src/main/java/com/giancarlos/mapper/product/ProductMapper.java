package com.giancarlos.mapper.product;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDTO(Product product);
    Product toEntity(ProductDto productDto);
}
