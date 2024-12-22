package com.samic.commonService.events;

import lombok.Data;

@Data
public class ProductReservedEvent {
    private String productId;
    private String orderId;
    private int quantity;
}
