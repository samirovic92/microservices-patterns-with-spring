package com.samic.OrdersService.core.events;

import com.samic.OrdersService.command.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRejectedEvent {
    private String orderId;
    private String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
