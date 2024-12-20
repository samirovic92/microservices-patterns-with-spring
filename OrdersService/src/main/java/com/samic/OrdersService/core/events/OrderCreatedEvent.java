package com.samic.OrdersService.core.events;

import com.samic.OrdersService.command.OrderStatus;
import lombok.Data;

@Data
public class OrderCreatedEvent implements OrderEvent {
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
