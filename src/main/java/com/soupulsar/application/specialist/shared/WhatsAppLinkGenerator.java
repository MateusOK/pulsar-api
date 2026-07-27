package com.soupulsar.application.specialist.shared;

import org.springframework.stereotype.Component;

@Component
public class WhatsAppLinkGenerator {

    private static final String BASE_URL = "https://wa.me/";

    public String generate(String phoneNumber){
        if (phoneNumber == null || phoneNumber.isBlank()){
            return null;
        }
        return BASE_URL + phoneNumber.replaceAll("\\D", "");
    }
}