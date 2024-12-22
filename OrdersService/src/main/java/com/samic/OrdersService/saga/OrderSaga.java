package com.samic.OrdersService.saga;

import com.samic.OrdersService.core.events.OrderCreatedEvent;
import com.samic.commonService.command.ReserveProductCommand;
import com.samic.commonService.events.ProductReservedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class OrderSaga {
    private final Logger logger = LoggerFactory.getLogger(OrderSaga.class);
    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = new ReserveProductCommand();
        BeanUtils.copyProperties(orderCreatedEvent, reserveProductCommand);

        logger.info(
                String.format("OrderCreatedEvent event is handled for the orderId: %s", orderCreatedEvent.getOrderId())
        );

        this.commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if(commandResultMessage.isExceptional()){
                // start a compensating transaction
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        logger.info(
                String.format("ProductReservedEvent is handled for the orderId: %s and productId : %s",
                        productReservedEvent.getOrderId(),productReservedEvent.getProductId())
        );
    }
}
