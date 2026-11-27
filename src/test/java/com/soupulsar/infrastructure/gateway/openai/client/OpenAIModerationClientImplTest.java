package com.soupulsar.infrastructure.gateway.openai.client;

import com.openai.client.OpenAIClient;
import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;
import com.openai.services.blocking.ModerationService;
import com.soupulsar.infrastructure.exceptions.ReviewModerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OpenAIModerationClientImplTest {

    private ModerationService moderationService;
    private OpenAIModerationClientImpl client;

    @BeforeEach
    void setUp() {
        OpenAIClient openAIClient = mock(OpenAIClient.class);
        moderationService = mock(ModerationService.class);
        when(openAIClient.moderations()).thenReturn(moderationService);

        client = new OpenAIModerationClientImpl(openAIClient);
    }

    @Test
    void shouldUseConfiguredModelAndReturnFlaggedStatus() {
        Moderation moderation = mock(Moderation.class);
        when(moderation.flagged()).thenReturn(true);

        ModerationCreateResponse apiResponse = mock(ModerationCreateResponse.class);
        when(apiResponse.results()).thenReturn(List.of(moderation));

        when(moderationService.create(any(ModerationCreateParams.class))).thenReturn(apiResponse);

        OpenAIModerationResponse response = client.moderate("Texto impróprio");

        assertTrue(response.flagged());

        ArgumentCaptor<ModerationCreateParams> paramsCaptor = ArgumentCaptor.forClass(ModerationCreateParams.class);
        verify(moderationService).create(paramsCaptor.capture());
        assertEquals("omni-moderation-latest", paramsCaptor.getValue().model().orElseThrow().asString());
    }

    @Test
    void shouldReturnUnflaggedWhenOpenAiApprovesComment() {
        Moderation moderation = mock(Moderation.class);
        when(moderation.flagged()).thenReturn(false);

        ModerationCreateResponse apiResponse = mock(ModerationCreateResponse.class);
        when(apiResponse.results()).thenReturn(List.of(moderation));

        when(moderationService.create(any(ModerationCreateParams.class))).thenReturn(apiResponse);

        OpenAIModerationResponse response = client.moderate("Excelente especialista");

        assertFalse(response.flagged());
    }

    @Test
    void shouldThrowReviewModerationExceptionWhenResultsAreEmpty() {
        ModerationCreateResponse apiResponse = mock(ModerationCreateResponse.class);
        when(apiResponse.results()).thenReturn(List.of());

        when(moderationService.create(any(ModerationCreateParams.class))).thenReturn(apiResponse);

        assertThrows(ReviewModerationException.class, () -> client.moderate("Comentário"));
    }

    @Test
    void shouldPropagateExceptionWhenOpenAiClientFails() {
        when(moderationService.create(any(ModerationCreateParams.class)))
                .thenThrow(new RuntimeException("API failure"));

        assertThrows(RuntimeException.class, () -> client.moderate("Comentário"));
    }
}
