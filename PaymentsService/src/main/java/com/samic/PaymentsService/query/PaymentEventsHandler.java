package com.samic.PaymentsService.query;

import com.samic.PaymentsService.core.data.PaymentEntity;
import com.samic.PaymentsService.core.data.PaymentRepository;
import com.samic.commonService.events.PaymentProcessedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaymentEventsHandler {
    private PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        PaymentEntity entity = new PaymentEntity(event.getPaymentId(), event.getOrderId());
        this.paymentRepository.save(entity);
    }
}
