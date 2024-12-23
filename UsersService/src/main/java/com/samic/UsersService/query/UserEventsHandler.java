package com.samic.UsersService.query;

import com.samic.commonService.model.PaymentDetails;
import com.samic.commonService.model.User;
import com.samic.commonService.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    @QueryHandler
    public User handle(FetchUserPaymentDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("Samir IDRISSI")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        return User.builder()
                .firstName("Samir")
                .lastName("Idrissi")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();
    }
}
