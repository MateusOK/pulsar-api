package com.soupulsar.application.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewContentValidatorTest {

    private ReviewContentValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ReviewContentValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Excelente profissional! Muito atencioso e pontual.",
            "Ótima sessão, me senti muito acolhido. Recomendo 100%!",
            "Nota 5! Me ajudou bastante nos últimos 6 meses.",
            "Consulta no dia 15/07/2026 às 14:30 foi maravilhosa.",
            "Já fiz 12 sessões com o Dr. João e só tenho a agradecer.",
            "O valor de R$ 150,00 valeu cada centavo.",
            "Atendimento nota 10/10, super recomendo."
    })
    void shouldAllowLegitimateComments(String comment) {
        assertDoesNotThrow(() -> validator.validate(comment));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Meu email é joao.silva@gmail.com",
            "Fale comigo em maria_123+tag@empresa.com.br",
            "Meu telefone é (11) 98765-4321",
            "Me liga no +55 11 98765-4321",
            "WhatsApp 11 987654321",
            "Pode chamar no 11987654321",
            "Meu fixo é (21) 3456-7890",
            "Me chama no 98765-4321",
            "Meu CPF é 123.456.789-00",
            "CPF: 12345678900",
            "CPF 123456789-00",
            "Meu RG é 12.345.678-9",
            "RG: 12345678",
            "Meu CEP é 01310-100",
            "Cartão 4111 2222 3333 4444",
            "SSN 123-45-6789"
    })
    void shouldRejectCommentsContainingSensitivePersonalInformation(String comment) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(comment));
    }

    @Test
    void shouldRejectNullOrBlankComments() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> validator.validate("   "));
    }
}
