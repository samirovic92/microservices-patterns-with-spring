package com.samic.OrdersService.config;

import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public DeadlineManager deadlineManager(org.axonframework.config.Configuration configuration,
                                           SpringTransactionManager transactionManager) {

        return SimpleDeadlineManager.builder()
                .scopeAwareProvider( new ConfigurationScopeAwareProvider(configuration))
                .transactionManager(transactionManager)
                .build();
    }
}
