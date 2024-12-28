package com.samic.OrdersService.query.rest;

import com.samic.OrdersService.command.OrderStatus;
import com.samic.OrdersService.core.data.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String orderId;
    private OrderStatus orderStatus;
    private String reason;

    public static OrderDto toDto(OrderEntity orderEntity) {
        return new OrderDto(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
    }
}
