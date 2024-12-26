package com.samic.OrdersService.saga;

import com.samic.OrdersService.command.commands.ApproveOrderCommand;
import com.samic.OrdersService.command.commands.RejectOrderCommand;
import com.samic.OrdersService.core.events.OrderApprovedEvent;
import com.samic.OrdersService.core.events.OrderCreatedEvent;
import com.samic.OrdersService.core.events.OrderRejectedEvent;
import com.samic.commonService.command.CancelProductReservationCommand;
import com.samic.commonService.command.ProcessPaymentCommand;
import com.samic.commonService.command.ReserveProductCommand;
import com.samic.commonService.events.PaymentProcessedEvent;
import com.samic.commonService.events.ProductReservationCancelledEvent;
import com.samic.commonService.events.ProductReservedEvent;
import com.samic.commonService.model.User;
import com.samic.commonService.query.FetchUserPaymentDetailsQuery;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Saga
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private DeadlineManager deadlineManager;

    private static final Logger logger = LoggerFactory.getLogger(OrderSaga.class);
    private static final String PAYMENT_PROCESSING_DEADLINE = "payment-processing-deadline";
    private String scheduleId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        logger.info(String.format("OrderCreatedEvent event is handled for the orderId: %s", orderCreatedEvent.getOrderId()));
        ReserveProductCommand reserveProductCommand = new ReserveProductCommand();
        BeanUtils.copyProperties(orderCreatedEvent, reserveProductCommand);
        this.commandGateway.send(reserveProductCommand, reserveProductCommandCallback(orderCreatedEvent.getOrderId()));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        logger.info(
                String.format("ProductReservedEvent is handled for the orderId: %s and productId : %s",
                        productReservedEvent.getOrderId(), productReservedEvent.getProductId())
        );

        var userId = productReservedEvent.getUserId();
        try {
            FetchUserPaymentDetailsQuery userPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(userId);
            User user = this.queryGateway.query(userPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
            if (Objects.isNull(user)) {
                cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
                return;
            }

            logger.info("Successfully fetch user details for user : " + user);

            this.scheduleId = deadlineManager.schedule(Duration.ofSeconds(10), PAYMENT_PROCESSING_DEADLINE, productReservedEvent);

            ProcessPaymentCommand processPaymentCommand = buildProcessPaymentCommand(productReservedEvent, user);
            commandGateway.send(processPaymentCommand, processPaymentCommandCallback(productReservedEvent));

        } catch (Exception e) {
            // compensating Transaction
            cancelProductReservation(productReservedEvent, e.getMessage());
            return;
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent paymentProcessedEvent) {
        cancelDeadLine();
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        this.commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderApprovedEvent orderApprovedEvent) {
        logger.info("Order is Approved. Order saga is ended for orderId : " + orderApprovedEvent.getOrderId());
        // SagaLifecycle.end();
    }

    // ------------ Compensating event ------------
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
        rejectOrder(productReservationCancelledEvent.getOrderId(), productReservationCancelledEvent.getReason());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent) {
        logger.info("Successfully rejected order with id " + orderRejectedEvent.getOrderId());
    }

    // ------------ DeadLine ------------
    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
    public void handlePaymentDeadLine(ProductReservedEvent productReservedEvent) {
        logger.info("Payment deadLine took place.");
        cancelProductReservation(productReservedEvent, "Processing Payment timeout");
    }

    private void cancelDeadLine() {
        if(nonNull(scheduleId)) {
            deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId);
            scheduleId = null;
        }
    }

    private CommandCallback<ReserveProductCommand, Object> reserveProductCommandCallback(String orderId) {
        return (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // compensating transaction
                rejectOrder(orderId, commandResultMessage.exceptionResult().getMessage());
            }
        };
    }

    private CommandCallback<ProcessPaymentCommand, Object> processPaymentCommandCallback(ProductReservedEvent productReservedEvent) {
        return (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // compensating transaction
                cancelProductReservation(productReservedEvent, "Could not proccess user payment with provided payment details");
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

    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
        cancelDeadLine();
        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .productId(productReservedEvent.getProductId())
                .userId(productReservedEvent.getUserId())
                .quantity(productReservedEvent.getQuantity())
                .reason(reason)
                .build();
        commandGateway.send(cancelProductReservationCommand);
    }

    private void rejectOrder(String orderId, String reason) {
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderId, reason);
        commandGateway.send(rejectOrderCommand);
    }
}
