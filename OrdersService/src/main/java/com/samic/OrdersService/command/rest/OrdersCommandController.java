package com.samic.OrdersService.command.rest;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        var createOrderCommand = createOrderRequest.toCommand();
        String returnValue = this.commandGateway.sendAndWait(createOrderCommand);
        return returnValue;
    }
}
