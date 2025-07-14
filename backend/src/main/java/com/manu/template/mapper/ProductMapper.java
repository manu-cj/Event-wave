package com.manu.template.mapper;

import com.manu.template.model.Product;
import com.manu.template.dto.ProductDTO;

public class ProductMapper {
    public static ProductDTO toDto(Product product) {
        if (product == null) return null;
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }

    public static Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .build();
    }
}