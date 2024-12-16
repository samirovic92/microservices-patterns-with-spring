package com.samic.ProductsService.command.rest;

import com.samic.ProductsService.command.CreateProductCommand;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProductRequest {
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public CreateProductCommand toCommand() {
        return CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .title(this.getTitle())
                .quantity(this.getQuantity())
                .price(this.getPrice())
                .build();
    }
}
