package com.soupulsar.application.config;

import com.asaas.apisdk.AsaasSdk;
import com.asaas.apisdk.config.ApiKeyAuthConfig;
import com.asaas.apisdk.config.AsaasSdkConfig;
import com.asaas.apisdk.config.RetryConfig;
import com.asaas.apisdk.services.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsaasConfig {

    @Value("${asaas.apiKey}")
    private String asaasApiKey;
    @Value("${asaas.baseUrl}")
    private String baseUrl;

    @Bean
    public AsaasSdk asaasSdk() {
        ApiKeyAuthConfig apiKeyAuthConfig = ApiKeyAuthConfig.builder()
                .apiKey(asaasApiKey)
                .apiKeyHeader("access_token")
                .build();

        AsaasSdkConfig asaasSdkConfig = AsaasSdkConfig.builder()
                .apiKeyAuthConfig(apiKeyAuthConfig)
                .baseUrl(baseUrl)
                .timeout(30000)
                .retryConfig(RetryConfig.builder().maxRetries(3).build())
                .build();

        return new AsaasSdk(asaasSdkConfig);
    }

    @Bean
    public CustomerService customerService(AsaasSdk asaasSdk) {
        return asaasSdk.customer;
    }

}