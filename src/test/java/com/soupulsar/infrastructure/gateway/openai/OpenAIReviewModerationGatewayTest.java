package com.soupulsar.infrastructure.gateway.openai;

import com.soupulsar.application.dto.response.ReviewModerationResult;
import com.soupulsar.infrastructure.exceptions.ReviewModerationException;
import com.soupulsar.infrastructure.gateway.openai.client.OpenAIModerationClient;
import com.soupulsar.infrastructure.gateway.openai.client.OpenAIModerationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIReviewModerationGatewayTest {

    @Mock
    private OpenAIModerationClient openAIModerationClient;

    private OpenAIReviewModerationGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new OpenAIReviewModerationGateway(openAIModerationClient);
    }

    @Test
    void shouldReturnUnflaggedResultWhenCommentIsAppropriate() {
        when(openAIModerationClient.moderate("Excelente consulta"))
                .thenReturn(new OpenAIModerationResponse(false));

        ReviewModerationResult result = gateway.moderate("Excelente consulta");

        assertFalse(result.flagged());
        verify(openAIModerationClient).moderate("Excelente consulta");
    }

    @Test
    void shouldReturnFlaggedResultWhenCommentIsInappropriate() {
        when(openAIModerationClient.moderate("Comentário ofensivo"))
                .thenReturn(new OpenAIModerationResponse(true));

        ReviewModerationResult result = gateway.moderate("Comentário ofensivo");

        assertTrue(result.flagged());
        verify(openAIModerationClient).moderate("Comentário ofensivo");
    }

    @Test
    void shouldThrowReviewModerationExceptionWhenClientFails() {
        when(openAIModerationClient.moderate("Comentário"))
                .thenThrow(new RuntimeException("OpenAI connection error"));

        assertThrows(ReviewModerationException.class, () -> gateway.moderate("Comentário"));
    }

    @Test
    void shouldThrowReviewModerationExceptionWhenClientReturnsNull() {
        when(openAIModerationClient.moderate("Comentário")).thenReturn(null);

        assertThrows(ReviewModerationException.class, () -> gateway.moderate("Comentário"));
    }
}
