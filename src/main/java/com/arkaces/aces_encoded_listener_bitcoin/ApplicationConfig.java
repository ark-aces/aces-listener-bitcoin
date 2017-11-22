package com.arkaces.aces_encoded_listener_bitcoin;

import com.arkaces.aces_api_server_lib.identifer.IdentifierGenerator;
import com.arkaces.aces_api_server_lib.json.NiceObjectMapper;
import com.arkaces.aces_encoded_listener_bitcoin.bitcoin.BitcoinRpcRequestFactory;
import com.arkaces.aces_encoded_listener_bitcoin.bitcoin.BitcoinRpcSettings;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class ApplicationConfig {

    @Bean
    public RestTemplate callbackRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.indentOutput(true);
        return builder;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator();
    }

    @Bean
    public Integer maxScanBlockDepth(Environment environment) {
        return environment.getProperty("maxScanBlockDepth", Integer.class);
    }

    @Bean
    public NiceObjectMapper logObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return new NiceObjectMapper(objectMapper);
    }

    @Bean
    public NiceObjectMapper dtoObjectMapper() {
        return new NiceObjectMapper(new ObjectMapper());
    }

    @Bean
    public RestTemplate bitcoinRpcRestTemplate(BitcoinRpcSettings bitcoinRpcSettings) {
        return new RestTemplateBuilder()
            .rootUri("http://localhost:18332/")
            .basicAuthorization(bitcoinRpcSettings.getUsername(), bitcoinRpcSettings.getPassword())
            .build();
    }


}
