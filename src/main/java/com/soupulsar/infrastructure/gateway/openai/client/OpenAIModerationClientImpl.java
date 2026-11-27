package com.soupulsar.infrastructure.gateway.openai.client;

import com.openai.client.OpenAIClient;
import com.openai.models.moderations.ModerationCreateParams;
import com.soupulsar.infrastructure.exceptions.ReviewModerationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OpenAIModerationClientImpl implements OpenAIModerationClient {

    private final OpenAIClient openAIClient;

    private static final String MODEL = "omni-moderation-latest";


    @Override
    public OpenAIModerationResponse moderate(String comment) {
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Comment for moderation cannot be blank");
        }

        var params = ModerationCreateParams.builder()
                .model(MODEL)
                .input(comment)
                .build();

        var response = openAIClient.moderations().create(params);

        if (response == null || response.results() == null) {
            throw new ReviewModerationException("Empty moderation response from OpenAI");
        }

        var result = response.results()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ReviewModerationException("No moderation result found"));

        return new OpenAIModerationResponse(result.flagged());
    }
}