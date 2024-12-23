package com.samic.OrdersService.core.events;

import com.samic.OrdersService.command.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderApprovedEvent {
    private String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
