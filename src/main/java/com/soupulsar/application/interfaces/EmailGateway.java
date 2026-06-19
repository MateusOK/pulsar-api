package com.soupulsar.application.interfaces;

public interface EmailGateway {

    void sendPasswordResetEmail(String resetLink, String name, String email);

}