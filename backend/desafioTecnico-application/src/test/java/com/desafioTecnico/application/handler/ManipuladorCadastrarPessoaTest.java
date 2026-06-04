package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.interfaces.IPortaGeradorLogin;
import com.desafioTecnico.application.interfaces.IPortaServicoEndereco;
import com.desafioTecnico.application.interfaces.IPortaValidadorCpf;
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

    @Mock IRepositorioPessoa repositorioPessoa;
    @Mock IPortaGeradorLogin geradorLogin;
    @Mock IPortaServicoEndereco servicoEndereco;
    @Mock IPortaValidadorCpf validadorCpf;

    HandlerCadastrarPessoa manipulador;

    @BeforeEach
    void setUp() {
        manipulador = new HandlerCadastrarPessoa(repositorioPessoa, geradorLogin, servicoEndereco, validadorCpf);
    }

    private CommandCadastrarPessoa comandoValido() {
        CommandCadastrarPessoa cmd = new CommandCadastrarPessoa();
        cmd.setNomeCompleto("Maria Silva");
        cmd.setCpf("529.982.247-25");
        cmd.setEmail("maria@example.com");
        cmd.setDataNascimento(LocalDate.of(1990, 1, 1));
        cmd.setCep("01310100");
        cmd.setNumero("100");
        return cmd;
    }

    private Pessoa pessoaSalva() {
        return new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Avenida Paulista",
                "Bela Vista", "Sao Paulo", "SP", null, "100", "msilvaa", null
        );
    }

    private Map<String, String> enderecoStub() {
        return Map.of("cep","01310100","logradouro","Avenida Paulista","bairro","Bela Vista","cidade","Sao Paulo","estado","SP");
    }

    @Test
    void deveCadastrarPessoaComSucesso() {
        when(validadorCpf.validarENormalizar("529.982.247-25")).thenReturn("52998224725");
        when(repositorioPessoa.buscarPorCpf("52998224725")).thenReturn(Optional.empty());
        when(repositorioPessoa.buscarPorEmail("maria@example.com")).thenReturn(Optional.empty());
        when(servicoEndereco.buscarPorCep("01310100")).thenReturn(enderecoStub());
        when(repositorioPessoa.listarTodosLogins()).thenReturn(List.of());
        when(geradorLogin.gerar(eq("Maria Silva"), anyList())).thenReturn("msilvaa");
        when(repositorioPessoa.salvar(any())).thenReturn(pessoaSalva());

        PessoaDto resposta = manipulador.handle(comandoValido());

        assertNotNull(resposta);
        assertEquals("Maria Silva", resposta.getNomeCompleto());
        verify(repositorioPessoa).salvar(any(Pessoa.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        when(validadorCpf.validarENormalizar("529.982.247-25")).thenReturn("52998224725");
        when(repositorioPessoa.buscarPorCpf("52998224725")).thenReturn(Optional.of(pessoaSalva()));

        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () -> manipulador.handle(comandoValido()));
        assertTrue(ex.getMessage().contains("CPF"));
        verify(repositorioPessoa, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        when(validadorCpf.validarENormalizar("529.982.247-25")).thenReturn("52998224725");
        when(repositorioPessoa.buscarPorCpf("52998224725")).thenReturn(Optional.empty());
        when(repositorioPessoa.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(pessoaSalva()));

        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () -> manipulador.handle(comandoValido()));
        assertTrue(ex.getMessage().contains("e-mail"));
        verify(repositorioPessoa, never()).salvar(any());
    }

    @Test
    void devePassarComplementoENumeroParaPessoa() {
        CommandCadastrarPessoa comandoComComplemento = new CommandCadastrarPessoa();
        comandoComComplemento.setNomeCompleto("Maria Silva");
        comandoComComplemento.setCpf("529.982.247-25");
        comandoComComplemento.setEmail("maria@example.com");
        comandoComComplemento.setDataNascimento(LocalDate.of(1990, 1, 1));
        comandoComComplemento.setCep("01310100");
        comandoComComplemento.setComplemento("Apto 42");
        comandoComComplemento.setNumero("200");

        when(validadorCpf.validarENormalizar(any())).thenReturn("52998224725");
        when(repositorioPessoa.buscarPorCpf(any())).thenReturn(Optional.empty());
        when(repositorioPessoa.buscarPorEmail(any())).thenReturn(Optional.empty());
        when(servicoEndereco.buscarPorCep(any())).thenReturn(enderecoStub());
        when(repositorioPessoa.listarTodosLogins()).thenReturn(List.of());
        when(geradorLogin.gerar(any(), any())).thenReturn("msilvaa");
        when(repositorioPessoa.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        PessoaDto resposta = manipulador.handle(comandoComComplemento);
        assertNotNull(resposta);
    }
}
