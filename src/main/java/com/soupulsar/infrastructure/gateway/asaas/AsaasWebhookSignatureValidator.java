package com.soupulsar.infrastructure.gateway.asaas;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AsaasWebhookSignatureValidator {

    private final String webhookToken;

    public AsaasWebhookSignatureValidator(@Value("${asaas.webhookToken}") String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public boolean isValid(String tokenFromHeader){
        if (tokenFromHeader == null || tokenFromHeader.isEmpty()){
            return false;
        }
        return webhookToken.equals(tokenFromHeader);
    }
}