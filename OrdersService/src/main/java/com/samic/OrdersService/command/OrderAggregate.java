package com.samic.OrdersService.command;

import com.samic.OrdersService.command.commands.ApproveOrderCommand;
import com.samic.OrdersService.command.commands.CreateOrderCommand;
import com.samic.OrdersService.command.commands.RejectOrderCommand;
import com.samic.OrdersService.core.events.OrderApprovedEvent;
import com.samic.OrdersService.core.events.OrderCreatedEvent;
import com.samic.OrdersService.core.events.OrderRejectedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {
    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand) {
        OrderApprovedEvent orderCreatedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
        OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(
                rejectOrderCommand.getOrderId(), rejectOrderCommand.getReason()
        );
        AggregateLifecycle.apply(orderRejectedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.orderId;
        this.userId = orderCreatedEvent.getUserId();
        this.productId = orderCreatedEvent.getProductId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.addressId = orderCreatedEvent.getAddressId();
        this.orderStatus = OrderStatus.CREATED;
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent orderCreatedEvent) {
        this.orderStatus = OrderStatus.APPROVED;
    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent ) {
        this.orderStatus = orderRejectedEvent.getOrderStatus();
    }
}
