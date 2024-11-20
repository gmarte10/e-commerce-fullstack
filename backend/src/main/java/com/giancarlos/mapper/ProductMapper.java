package com.giancarlos.mapper;

import com.giancarlos.dto.ProductDto;
import com.giancarlos.dto.UserDto;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDTO(Product product);
    Product toEntity(ProductDto productDto);
}
