package com.samic.commonService.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelProductReservationCommand {
    @TargetAggregateIdentifier
    private String productId;
    private String orderId;
    private String userId;
    private int quantity;
    private String reason;
}
