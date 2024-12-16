package com.samic.ProductsService.query.rest;

import com.samic.ProductsService.core.data.ProductEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public static List<ProductDto> toDtos(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(ProductDto::toDto)
                .toList();
    }
    public static ProductDto toDto(ProductEntity productEntity) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(productEntity, productDto);
        return productDto;
    }

}
