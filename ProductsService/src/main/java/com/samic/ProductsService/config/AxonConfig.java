package com.samic.ProductsService.config;

import com.samic.ProductsService.command.interceptor.ProductCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Autowired
    public void configureCommandBus(CommandBus commandBus, ProductCommandInterceptor productCommandInterceptor) {
        commandBus.registerDispatchInterceptor(productCommandInterceptor);
    }

    @Autowired
    public void configureErrorHandler(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler(
                "products-group",
                configuration -> PropagatingErrorHandler.instance()
        );
    }
}
