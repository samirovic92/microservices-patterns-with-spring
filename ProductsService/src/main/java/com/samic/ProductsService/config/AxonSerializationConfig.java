package com.samic.ProductsService.config;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonSerializationConfig {

    @Primary
    @Bean
    public Serializer serializer() {
        return JacksonSerializer.defaultSerializer();
    }
}
