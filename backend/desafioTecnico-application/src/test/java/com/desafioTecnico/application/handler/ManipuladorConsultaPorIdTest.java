package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPessoa;
import com.desafioTecnico.application.mediator.query.QueryPessoa;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerQueryPessoaTest {

    @Mock IRepositorioPessoa repositorioPessoa;

    HandlerQueryPessoa manipulador;

    @BeforeEach
    void setUp() {
        manipulador = new HandlerQueryPessoa(repositorioPessoa);
    }

    private Pessoa criarPessoa(UUID id) {
        return new Pessoa(
                id, "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        );
    }

    @Test
    void deveRetornarPessoaQuandoEncontradaPorId() {
        UUID id = UUID.randomUUID();
        when(repositorioPessoa.buscarPorId(any(UUID.class))).thenReturn(Optional.of(criarPessoa(id)));

        PessoaDto resposta = manipulador.handle(new QueryPessoa(id.toString()));

        assertEquals("Maria Silva", resposta.getNomeCompleto());
        assertEquals(id.toString(), resposta.getId());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontradaPorId() {
        when(repositorioPessoa.buscarPorId(any(UUID.class))).thenReturn(Optional.empty());

        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> manipulador.handle(new QueryPessoa(UUID.randomUUID().toString())));

        assertTrue(ex.getMessage().contains("não encontrada"));
    }
}
