package com.samic.ProductsService.command.interceptor;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class ProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final Logger logger = LoggerFactory.getLogger(ProductCommandInterceptor.class);

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {
            logger.info(" command name : " + command.getCommandName());
            logger.info(" command data : " + command.getPayload());
            return command;
        };
    }
}
