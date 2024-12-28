package com.samic.OrdersService.query;

import com.samic.OrdersService.core.data.OrderEntity;
import com.samic.OrdersService.core.data.OrderRepository;
import com.samic.OrdersService.query.query.FindOrderQuery;
import com.samic.OrdersService.query.rest.OrderDto;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import static com.samic.OrdersService.query.rest.OrderDto.toDto;

@AllArgsConstructor
@Component
public class OrderQueriesHandler {
    private final OrderRepository orderRepository;

    @QueryHandler
    public OrderDto findOrder(FindOrderQuery findOrderQuery) {
        return orderRepository.findById(findOrderQuery.getOrderId())
                .map(OrderDto::toDto)
                .orElse(null);
    }
}
