package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.handler.HandlerTodasPessoas;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerTodasPessoasTest {

    @Mock IRepositorioPessoa repositorioPessoa;

    HandlerTodasPessoas manipulador;

    @BeforeEach
    void setUp() {
        manipulador = new HandlerTodasPessoas(repositorioPessoa);
    }

    private Pessoa criarPessoa(String nome, String cpf, String email, String login) {
        return new Pessoa(
                UUID.randomUUID(), nome, cpf, email,
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, login, null
        );
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPessoas() {
        when(repositorioPessoa.listarTodos()).thenReturn(List.of());

        List<PessoaDto> resultado = manipulador.handle(new QueryTodasPessoas());

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRetornarTodasAsPessoas() {
        Pessoa p1 = criarPessoa("Maria Silva", "52998224725", "maria@example.com", "msilvaa");
        Pessoa p2 = criarPessoa("Joao Lima", "98765432100", "joao@example.com", "jlimabb");

        when(repositorioPessoa.listarTodos()).thenReturn(List.of(p1, p2));

        List<PessoaDto> resultado = manipulador.handle(new QueryTodasPessoas());

        assertEquals(2, resultado.size());
        assertEquals("Maria Silva", resultado.get(0).getNomeCompleto());
        assertEquals("Joao Lima", resultado.get(1).getNomeCompleto());
    }
}
