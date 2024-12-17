package com.samic.ProductsService.command.rest;

import com.samic.ProductsService.command.CreateProductCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product title is required field")
    private String title;
    @Min(value = 1, message = "Price cannot be lower than 1")
    private BigDecimal price;
    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Max(value = 5, message = "Quantity cannot be greater than 5")
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
