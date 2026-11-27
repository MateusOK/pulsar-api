package com.soupulsar.infrastructure.gateway.openai;

import com.soupulsar.application.dto.response.ReviewModerationResult;
import com.soupulsar.application.interfaces.ReviewModerationGateway;
import com.soupulsar.infrastructure.exceptions.ReviewModerationException;
import com.soupulsar.infrastructure.gateway.openai.client.OpenAIModerationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIReviewModerationGateway implements ReviewModerationGateway {

    private final OpenAIModerationClient openAIModerationClient;

    @Override
    public ReviewModerationResult moderate(String comment) {
        try {
            var response = openAIModerationClient.moderate(comment);
            if (response == null) {
                throw new ReviewModerationException("Moderation client returned null response");
            }
            return new ReviewModerationResult(response.flagged());
        } catch (ReviewModerationException e) {
            log.error("OpenAI moderation failed: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to moderate review comment via OpenAI", e);
            throw new ReviewModerationException("Failed to moderate review comment via OpenAI", e);
        }
    }
}