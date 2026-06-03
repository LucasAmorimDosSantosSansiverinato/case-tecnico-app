package com.desafioTecnico.webui.handler;

import com.desafioTecnico.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturn422ForDomainException() {
        DomainException ex = new DomainException("Business rule violated");
        ProblemDetail detail = handler.handleDomainException(ex);

        assertEquals(422, detail.getStatus());
        assertEquals("Business Rule Violation", detail.getTitle());
        assertEquals("Business rule violated", detail.getDetail());
    }

    @Test
    void shouldReturn400ForValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("req", "fullName", "Full name is required");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ProblemDetail detail = handler.handleValidation(ex);

        assertEquals(400, detail.getStatus());
        assertEquals("Invalid Request", detail.getTitle());
        assertNotNull(detail.getProperties());
        assertTrue(detail.getProperties().containsKey("errors"));
    }

    @Test
    void shouldReturn400ForIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("bad argument");
        ProblemDetail detail = handler.handleIllegalArgument(ex);

        assertEquals(400, detail.getStatus());
        assertEquals("Invalid Argument", detail.getTitle());
    }

    @Test
    void shouldReturn500ForGenericException() {
        Exception ex = new Exception("something went wrong");
        ProblemDetail detail = handler.handleGeneric(ex);

        assertEquals(500, detail.getStatus());
        assertEquals("Internal Server Error", detail.getTitle());
        assertEquals("An unexpected error occurred", detail.getDetail());
    }
}
