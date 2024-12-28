package com.samic.OrdersService.query.rest;

import com.samic.OrdersService.core.data.OrderEntity;
import com.samic.OrdersService.query.query.FindOrderQuery;
import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.samic.OrdersService.query.rest.OrderDto.toDto;
import static java.util.Objects.requireNonNull;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderQueryController {
    private QueryGateway queryGateway;

    @GetMapping("/{orderId}")
    public OrderDto findOrderById(@PathVariable String orderId) {
        try (
                var queryResult = queryGateway.subscriptionQuery(
                        new FindOrderQuery(orderId),
                        ResponseTypes.instanceOf(OrderDto.class),
                        ResponseTypes.instanceOf(OrderDto.class)
                )
        ) {
            return queryResult.updates().blockFirst();
        }
    }
}
