package com.desafioTecnico.domain;

import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void shouldAcceptValidCpf() {
        Document doc = Document.of("529.982.247-25");
        assertEquals("52998224725", doc.getValue());
        assertEquals("529.982.247-25", doc.getFormatted());
    }

    @Test
    void shouldAcceptRawDigits() {
        Document doc = Document.of("52998224725");
        assertEquals("52998224725", doc.getValue());
    }

    @Test
    void shouldRejectAllSameDigits() {
        assertThrows(DomainException.class, () -> Document.of("11111111111"));
    }

    @Test
    void shouldRejectInvalidCheckDigit() {
        assertThrows(DomainException.class, () -> Document.of("52998224726"));
    }

    @Test
    void shouldRejectWrongLength() {
        assertThrows(DomainException.class, () -> Document.of("1234567890"));
    }
}
