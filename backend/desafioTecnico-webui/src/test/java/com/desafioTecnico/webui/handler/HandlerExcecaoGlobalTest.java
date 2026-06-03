package com.desafioTecnico.webui.handler;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.webui.excecao.HandlerExcecaoGlobal;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExcecaoGlobalTest {

    private final HandlerExcecaoGlobal manipulador = new HandlerExcecaoGlobal();

    @Test
    void deveRetornar422ParaExcecaoDominio() {
        ExcecaoDominio ex = new ExcecaoDominio("Regra de negócio violada");
        ProblemDetail detalhe = manipulador.tratarExcecaoDominio(ex);

        assertEquals(422, detalhe.getStatus());
        assertEquals("Violação de Regra de Negócio", detalhe.getTitle());
        assertEquals("Regra de negócio violada", detalhe.getDetail());
    }

    @Test
    void deveRetornar400ParaExcecaoDeValidacao() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("req", "nomeCompleto", "Nome completo é obrigatório");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ProblemDetail detalhe = manipulador.tratarValidacao(ex);

        assertEquals(400, detalhe.getStatus());
        assertEquals("Requisição Inválida", detalhe.getTitle());
        assertNotNull(detalhe.getProperties());
        assertTrue(detalhe.getProperties().containsKey("erros"));
    }

    @Test
    void deveRetornar400ParaArgumentoInvalido() {
        IllegalArgumentException ex = new IllegalArgumentException("argumento inválido");
        ProblemDetail detalhe = manipulador.tratarArgumentoInvalido(ex);

        assertEquals(400, detalhe.getStatus());
        assertEquals("Argumento Inválido", detalhe.getTitle());
    }

    @Test
    void deveRetornar500ParaExcecaoGenerica() {
        Exception ex = new Exception("algo deu errado");
        ProblemDetail detalhe = manipulador.tratarGenerico(ex);

        assertEquals(500, detalhe.getStatus());
        assertEquals("Erro Interno do Servidor", detalhe.getTitle());
        assertEquals("Ocorreu um erro inesperado", detalhe.getDetail());
    }
}
