package com.samic.ProductsService.command.interceptor;

import com.samic.ProductsService.command.commands.CreateProductCommand;
import com.samic.ProductsService.core.data.ProductLookupRepository;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@AllArgsConstructor
public class ProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final Logger logger = LoggerFactory.getLogger(ProductCommandInterceptor.class);
    private final ProductLookupRepository productLookupRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {
            logger.info(" command name : " + command.getCommandName());
            logger.info(" command data : " + command.getPayload());

            if(CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                var product = productLookupRepository.findByProductIdOrTitle(
                        createProductCommand.getProductId(), createProductCommand.getTitle()
                );
                if(product.isPresent()) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "The product with id %s or title %s is already exist",
                                    createProductCommand.getProductId(), createProductCommand.getTitle())
                    );
                }
            }
            return command;
        };
    }
}
