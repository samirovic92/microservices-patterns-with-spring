package com.samic.ProductsService.query;

import com.samic.ProductsService.core.data.ProductEntity;
import com.samic.ProductsService.core.data.ProductRepository;
import com.samic.ProductsService.core.events.ProductCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsHandler {

    private ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(event, entity);
        productRepository.save(entity);
    }
}
