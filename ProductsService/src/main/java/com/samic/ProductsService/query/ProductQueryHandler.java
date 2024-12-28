package com.samic.ProductsService.query;

import com.samic.ProductsService.core.data.ProductEntity;
import com.samic.ProductsService.core.data.ProductRepository;
import com.samic.ProductsService.query.queries.FindProductsQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductQueryHandler {

    private final ProductRepository productRepository;

    @QueryHandler
    public List<ProductEntity> getProducts(FindProductsQuery findProductsQuery) {
        return productRepository.findAll();
    }
}
