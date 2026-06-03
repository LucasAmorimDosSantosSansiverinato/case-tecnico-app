package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPorId;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorId;
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
class HandlerQueryPorIdTest {

    @Mock IRepositorioPessoa IRepositorioPessoa;

    HandlerQueryPorId manipulador;

    @BeforeEach
    void setUp() {
        manipulador = new HandlerQueryPorId(IRepositorioPessoa);
    }

    private Pessoa criarPessoa(UUID id) {
        return Pessoa.builder()
                .id(id)
                .nomeCompleto("Maria Silva")
                .cpf("52998224725")
                .email("maria@example.com")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .cep("01310100")
                .logradouro("Av Paulista")
                .cidade("Sao Paulo")
                .estado("SP")
                .login("msilvaa")
                .build();
    }

    @Test
    void deveRetornarPessoaQuandoEncontrada() {
        UUID id = UUID.randomUUID();
        when(IRepositorioPessoa.buscarPorId(any(UUID.class))).thenReturn(Optional.of(criarPessoa(id)));

        PessoaDto resposta = manipulador.handle(new QueryPessoaPorId(id.toString()));

        assertEquals("Maria Silva", resposta.getNomeCompleto());
        assertEquals(id.toString(), resposta.getId());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrada() {
        when(IRepositorioPessoa.buscarPorId(any(UUID.class))).thenReturn(Optional.empty());

        String uuid = UUID.randomUUID().toString();
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> manipulador.handle(new QueryPessoaPorId(uuid)));

        assertTrue(ex.getMessage().contains("não encontrada"));
    }
}
