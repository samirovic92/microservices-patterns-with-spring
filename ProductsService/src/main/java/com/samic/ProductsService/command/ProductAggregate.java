package com.samic.ProductsService.command;

import com.samic.ProductsService.command.commands.CreateProductCommand;
import com.samic.ProductsService.core.events.ProductCreatedEvent;
import com.samic.commonService.command.CancelProductReservationCommand;
import com.samic.commonService.command.ReserveProductCommand;
import com.samic.commonService.events.ProductReservationCancelledEvent;
import com.samic.commonService.events.ProductReservedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Objects;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
@AllArgsConstructor
@Data
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The price cannot be less or equal to zero");
        }
        if(Objects.nonNull(createProductCommand.getTitle()) && createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) {
        if(quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number of items in stock!");
        }

        ProductReservedEvent productReservedEvent = new ProductReservedEvent();
        BeanUtils.copyProperties(reserveProductCommand, productReservedEvent);
        AggregateLifecycle.apply(productReservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
        ProductReservationCancelledEvent productReservationCancelledEvent = new ProductReservationCancelledEvent();
        BeanUtils.copyProperties(cancelProductReservationCommand, productReservationCancelledEvent);
        AggregateLifecycle.apply(productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
        this.quantity += productReservationCancelledEvent.getQuantity();
    }
}
