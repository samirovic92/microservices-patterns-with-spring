package com.samic.ProductsService.query;

import com.samic.ProductsService.core.data.ProductEntity;
import com.samic.ProductsService.core.data.ProductRepository;
import com.samic.ProductsService.core.events.ProductCreatedEvent;
import com.samic.commonService.events.ProductReservationCancelledEvent;
import com.samic.commonService.events.ProductReservedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("products-group")
@AllArgsConstructor
public class ProductEventsHandler {
    private final Logger logger = LoggerFactory.getLogger(ProductEventsHandler.class);
    private final ProductRepository productRepository;

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(event, entity);
        productRepository.save(entity);
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        var productOptional = productRepository.findById(event.getProductId());
        if (productOptional.isPresent()) {
            var product = productOptional.get();
            product.setQuantity(product.getQuantity() - event.getQuantity());
            productRepository.save(product);
        }
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event) {
        var productOptional = productRepository.findById(event.getProductId());
        if (productOptional.isPresent()) {
            var product = productOptional.get();
            product.setQuantity(product.getQuantity() + event.getQuantity());
            productRepository.save(product);
        }
    }

    @ResetHandler
    public void reset() {
        this.productRepository.deleteAll();
    }
}
