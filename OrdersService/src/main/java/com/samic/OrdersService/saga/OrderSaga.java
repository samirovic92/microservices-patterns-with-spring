package com.samic.OrdersService.saga;

import com.samic.OrdersService.core.events.OrderCreatedEvent;
import com.samic.commonService.command.ProcessPaymentCommand;
import com.samic.commonService.command.ReserveProductCommand;
import com.samic.commonService.events.ProductReservedEvent;
import com.samic.commonService.model.User;
import com.samic.commonService.query.FetchUserPaymentDetailsQuery;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.UUID;

@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    private final Logger logger = LoggerFactory.getLogger(OrderSaga.class);


    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        logger.info(String.format("OrderCreatedEvent event is handled for the orderId: %s", orderCreatedEvent.getOrderId()));

        ReserveProductCommand reserveProductCommand = new ReserveProductCommand();
        BeanUtils.copyProperties(orderCreatedEvent, reserveProductCommand);

        this.commandGateway.send(reserveProductCommand, reserveProductCommandCallback());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        logger.info(
                String.format("ProductReservedEvent is handled for the orderId: %s and productId : %s",
                        productReservedEvent.getOrderId(),productReservedEvent.getProductId())
        );

        var userId = productReservedEvent.getUserId();
        try {

            FetchUserPaymentDetailsQuery userPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(userId);
            User user = this.queryGateway.query(userPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
            if(Objects.isNull(user)) {
                //start compensating Transaction
                return;
            }
            logger.info("Successfully fetch user details for user : " + user);
            ProcessPaymentCommand processPaymentCommand = buildProcessPaymentCommand(productReservedEvent, user);
            commandGateway.send(processPaymentCommand, processPaymentCommandCallback());

        } catch (Exception e) {
            logger.error(" Error when fetching user details for userId :" + userId, e.getMessage());
            // start compensating Transaction
            return;
        }
    }

    private CommandCallback<ReserveProductCommand, Object> reserveProductCommandCallback() {
        return (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // start a compensating transaction
            }
        };
    }

    private CommandCallback<ProcessPaymentCommand, Object> processPaymentCommandCallback() {
        return (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // start a compensating transaction
            }
        };
    }

    private ProcessPaymentCommand buildProcessPaymentCommand(ProductReservedEvent productReservedEvent, User user) {
        return ProcessPaymentCommand.builder()
                .paymentId(UUID.randomUUID().toString())
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(user.getPaymentDetails())
                .build();
    }
}
