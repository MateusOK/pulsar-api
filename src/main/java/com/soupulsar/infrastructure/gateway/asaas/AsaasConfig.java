package com.soupulsar.infrastructure.gateway.asaas;

import com.asaas.apisdk.AsaasSdk;
import com.asaas.apisdk.config.ApiKeyAuthConfig;
import com.asaas.apisdk.config.AsaasSdkConfig;
import com.asaas.apisdk.http.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsaasConfig {

    @Value("${asaas.apiKey}")
    private String asaasApiKey;

    @Bean
    public AsaasSdk asaasSdk() {
        AsaasSdkConfig config = AsaasSdkConfig.builder()
                .apiKeyAuthConfig(ApiKeyAuthConfig.builder().apiKey(asaasApiKey).build())
                .build();

        config.setEnvironment(Environment.SANDBOX);
        return new AsaasSdk(config);
    }
}