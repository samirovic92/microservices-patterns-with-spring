package com.samic.OrdersService.query;

import com.samic.OrdersService.core.data.OrderEntity;
import com.samic.OrdersService.core.data.OrderRepository;
import com.samic.OrdersService.core.events.OrderCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("orders-group")
@AllArgsConstructor
public class OrderEventsHandler {
    private final OrderRepository orderRepository;

    @ExceptionHandler
    public void handle( Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent, orderEntity);
        orderRepository.save(orderEntity);
    }
}
