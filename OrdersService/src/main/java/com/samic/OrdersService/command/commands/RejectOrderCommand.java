package com.samic.OrdersService.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String reason;
}
