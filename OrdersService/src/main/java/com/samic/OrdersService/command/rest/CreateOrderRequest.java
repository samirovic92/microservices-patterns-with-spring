package com.samic.OrdersService.command.rest;

import com.samic.OrdersService.command.commands.CreateOrderCommand;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {
    private String productId;
    private int quantity;
    private String addressId;

    public CreateOrderCommand toCommand() {
        return CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .productId(this.productId)
                .quantity(this.quantity)
                .addressId(this.addressId)
                .build();
    }
}
