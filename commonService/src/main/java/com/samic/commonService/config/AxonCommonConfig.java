package com.samic.commonService.config;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonCommonConfig {

    @Primary
    @Bean
    public Serializer serializer() {
        return JacksonSerializer.defaultSerializer();
    }
}
