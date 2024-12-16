package com.samic.ProductsService.events;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreatedEvent implements ProductEvent {
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
