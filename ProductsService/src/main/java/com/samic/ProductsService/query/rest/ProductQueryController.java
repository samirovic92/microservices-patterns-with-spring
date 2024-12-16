package com.samic.ProductsService.query.rest;

import com.samic.ProductsService.command.rest.CreateProductRequest;
import com.samic.ProductsService.core.data.ProductEntity;
import com.samic.ProductsService.query.FindProductsQuery;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.samic.ProductsService.query.rest.ProductDto.toDtos;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public List<ProductDto> getProducts() {
        FindProductsQuery findProductsQuery = new FindProductsQuery();
        List<ProductEntity> products = this.queryGateway.query(
                findProductsQuery,
                ResponseTypes.multipleInstancesOf(ProductEntity.class)
        ).join();
        return toDtos(products);
    }
}
