package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.interface_.IPortaGeradorLogin;
import com.desafioTecnico.application.interface_.IPortaServicoEndereco;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerCadastrarPessoa;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandlerCadastrarPessoaTest {

    @Mock IRepositorioPessoa IRepositorioPessoa;
    @Mock IPortaGeradorLogin geradorLogin;
    @Mock IPortaServicoEndereco servicoEndereco;

    HandlerCadastrarPessoa manipulador;

    @BeforeEach
    void setUp() {
        manipulador = new HandlerCadastrarPessoa(IRepositorioPessoa, geradorLogin, servicoEndereco);
    }

    private CommandCadastrarPessoa comandoValido() {
        return new CommandCadastrarPessoa(
                "Maria Silva",
                "529.982.247-25",
                "maria@example.com",
                LocalDate.of(1990, 1, 1),
                "01310100",
                null,
                "100"
        );
    }

    private Map<String, String> enderecoStub() {
        return Map.of(
                "cep", "01310100",
                "logradouro", "Avenida Paulista",
                "bairro", "Bela Vista",
                "cidade", "Sao Paulo",
                "estado", "SP"
        );
    }

    private Pessoa pessoaSalva() {
        return Pessoa.builder()
                .id(UUID.randomUUID())
                .nomeCompleto("Maria Silva")
                .cpf("52998224725")
                .email("maria@example.com")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .cep("01310100")
                .logradouro("Avenida Paulista")
                .bairro("Bela Vista")
                .cidade("Sao Paulo")
                .estado("SP")
                .login("msilvaa")
                .build();
    }

    @Test
    void deveCadastrarPessoaComSucesso() {
        when(IRepositorioPessoa.buscarPorCpf("52998224725")).thenReturn(Optional.empty());
        when(IRepositorioPessoa.buscarPorEmail("maria@example.com")).thenReturn(Optional.empty());
        when(servicoEndereco.buscarPorCep("01310100")).thenReturn(enderecoStub());
        when(IRepositorioPessoa.listarTodosLogins()).thenReturn(List.of());
        when(geradorLogin.gerar(eq("Maria Silva"), anyList())).thenReturn("msilvaa");
        when(IRepositorioPessoa.salvar(any())).thenReturn(pessoaSalva());

        PessoaDto resposta = manipulador.handle(comandoValido());

        assertNotNull(resposta);
        assertEquals("Maria Silva", resposta.getNomeCompleto());
        verify(IRepositorioPessoa).salvar(any(Pessoa.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        when(IRepositorioPessoa.buscarPorCpf("52998224725")).thenReturn(Optional.of(pessoaSalva()));

        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () -> manipulador.handle(comandoValido()));
        assertTrue(ex.getMessage().contains("CPF"));
        verify(IRepositorioPessoa, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        when(IRepositorioPessoa.buscarPorCpf("52998224725")).thenReturn(Optional.empty());
        when(IRepositorioPessoa.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(pessoaSalva()));

        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () -> manipulador.handle(comandoValido()));
        assertTrue(ex.getMessage().contains("e-mail"));
        verify(IRepositorioPessoa, never()).salvar(any());
    }

    @Test
    void devePropagExcecaoDoServicoDeEndereco() {
        when(IRepositorioPessoa.buscarPorCpf(any())).thenReturn(Optional.empty());
        when(IRepositorioPessoa.buscarPorEmail(any())).thenReturn(Optional.empty());
        when(servicoEndereco.buscarPorCep("01310100")).thenThrow(new ExcecaoDominio("CEP não encontrado"));

        assertThrows(ExcecaoDominio.class, () -> manipulador.handle(comandoValido()));
        verify(IRepositorioPessoa, never()).salvar(any());
    }

    @Test
    void devePassarComplementoENumeroParaEndereco() {
        CommandCadastrarPessoa comandoComComplemento = new CommandCadastrarPessoa(
                "Maria Silva", "529.982.247-25", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Apto 42", "200"
        );

        when(IRepositorioPessoa.buscarPorCpf(any())).thenReturn(Optional.empty());
        when(IRepositorioPessoa.buscarPorEmail(any())).thenReturn(Optional.empty());
        when(servicoEndereco.buscarPorCep("01310100")).thenReturn(enderecoStub());
        when(IRepositorioPessoa.listarTodosLogins()).thenReturn(List.of());
        when(geradorLogin.gerar(any(), any())).thenReturn("msilvaa");
        when(IRepositorioPessoa.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        PessoaDto resposta = manipulador.handle(comandoComComplemento);
        assertNotNull(resposta);
    }
}
