package com.samic.commonService.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
public class ReserveProductCommand {
    @TargetAggregateIdentifier
    private String productId;
    private String orderId;
    private String userId;
    private int quantity;
}
