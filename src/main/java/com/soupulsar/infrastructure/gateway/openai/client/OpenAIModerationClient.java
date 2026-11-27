package com.soupulsar.infrastructure.gateway.openai.client;

public interface OpenAIModerationClient {

    OpenAIModerationResponse moderate(String comment);
}