package com.samic.ProductsService.command;

import com.samic.ProductsService.core.data.ProductLookupEntity;
import com.samic.ProductsService.core.data.ProductLookupRepository;
import com.samic.ProductsService.core.events.ProductCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("products-group")
@AllArgsConstructor
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductLookupEntity entity = new ProductLookupEntity(productCreatedEvent.getProductId(), productCreatedEvent.getTitle());
        productLookupRepository.save(entity);
    }
}
