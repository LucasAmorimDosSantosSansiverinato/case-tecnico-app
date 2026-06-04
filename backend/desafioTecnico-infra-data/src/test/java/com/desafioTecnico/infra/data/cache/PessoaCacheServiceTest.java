package com.desafioTecnico.infra.data.cache;

import com.desafioTecnico.application.dto.EnderecoDto;
import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerTodasPessoas;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import com.desafioTecnico.application.mediator.query.QueryPessoa;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaCacheServiceTest {

    @Mock HandlerCadastrarPessoa manipuladorCadastrar;
    @Mock HandlerQueryPessoa manipuladorQuery;
    @Mock HandlerTodasPessoas manipuladorTodos;
    @Mock IRepositorioPessoa repositorioPessoa;

    PessoaCacheService cache;

    @BeforeEach
    void setUp() {
        cache = new PessoaCacheService(manipuladorCadastrar, manipuladorQuery, manipuladorTodos, repositorioPessoa);
    }

    private PessoaDto pessoaDto(String id, String login) {
        EnderecoDto endereco = new EnderecoDto("01310100", "Av Paulista", "Bela Vista", "Sao Paulo", "SP", null, "100");
        return new PessoaDto(id, "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), endereco, login, LocalDateTime.now());
    }

    // ── Busca por ID ─────────────────────────────────────────────────────────

    @Test
    void buscarPorIdDeveChamarHandlerNaPrimeiraChamada() {
        String id = UUID.randomUUID().toString();
        PessoaDto dto = pessoaDto(id, "msilvaa");
        when(manipuladorQuery.handle(any())).thenReturn(dto);

        PessoaDto resultado = cache.buscarPorId(new QueryPessoa(id));

        assertEquals(dto, resultado);
        verify(manipuladorQuery, times(1)).handle(any());
    }

    @Test
    void buscarPorIdNaoDeveIrAoBancoNaSegundaChamada() {
        String id = UUID.randomUUID().toString();
        when(manipuladorQuery.handle(any())).thenReturn(pessoaDto(id, "msilvaa"));

        cache.buscarPorId(new QueryPessoa(id));
        cache.buscarPorId(new QueryPessoa(id));

        // Handler chamado apenas uma vez — segunda usa cache
        verify(manipuladorQuery, times(1)).handle(any());
    }

    // ── Lista todas ───────────────────────────────────────────────────────────

    @Test
    void listarTodosDeveChamarHandlerNaPrimeiraChamada() {
        List<PessoaDto> lista = List.of(pessoaDto(UUID.randomUUID().toString(), "msilvaa"));
        when(manipuladorTodos.handle(any())).thenReturn(lista);

        List<PessoaDto> resultado = cache.listarTodos(new QueryTodasPessoas());

        assertEquals(lista, resultado);
        verify(manipuladorTodos, times(1)).handle(any());
    }

    @Test
    void listarTodosNaoDeveIrAoBancoNaSegundaChamada() {
        when(manipuladorTodos.handle(any())).thenReturn(List.of(pessoaDto(UUID.randomUUID().toString(), "msilvaa")));

        cache.listarTodos(new QueryTodasPessoas());
        cache.listarTodos(new QueryTodasPessoas());

        verify(manipuladorTodos, times(1)).handle(any());
    }

    // ── Cadastro ──────────────────────────────────────────────────────────────

    @Test
    void cadastrarDeveInserirNoCachePorId() {
        String id = UUID.randomUUID().toString();
        PessoaDto dto = pessoaDto(id, "msilvaa");
        when(manipuladorCadastrar.handle(any())).thenReturn(dto);

        cache.cadastrar(new CommandCadastrarPessoa());

        // Busca por id deve usar o cache — handler de query não deve ser chamado
        cache.buscarPorId(new QueryPessoa(id));
        verify(manipuladorQuery, never()).handle(any());
    }

    @Test
    void cadastrarDeveAdicionarNaListaCacheExistente() {
        // Popula a lista no cache primeiro
        String idExistente = UUID.randomUUID().toString();
        when(manipuladorTodos.handle(any())).thenReturn(List.of(pessoaDto(idExistente, "jlimabb")));
        cache.listarTodos(new QueryTodasPessoas());

        // Cadastra nova pessoa
        String novoId = UUID.randomUUID().toString();
        when(manipuladorCadastrar.handle(any())).thenReturn(pessoaDto(novoId, "msilvaa"));
        cache.cadastrar(new CommandCadastrarPessoa());

        // Lista em cache deve ter as duas sem ir ao banco novamente
        List<PessoaDto> listaAtualizada = cache.listarTodos(new QueryTodasPessoas());
        assertEquals(2, listaAtualizada.size());
        verify(manipuladorTodos, times(1)).handle(any());
    }
}
