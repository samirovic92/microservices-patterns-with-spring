package com.samic.ProductsService.config;

import com.samic.ProductsService.command.interceptor.ProductCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Autowired
    public void configureCommandBus(CommandBus commandBus, ProductCommandInterceptor productCommandInterceptor) {
        commandBus.registerDispatchInterceptor(productCommandInterceptor);
    }
}
