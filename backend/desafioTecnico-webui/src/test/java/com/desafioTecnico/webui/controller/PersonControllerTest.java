package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.dto.EnderecoDto;
import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.infra.data.cache.PessoaCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Testa o controller em isolamento total — sem contexto Spring, sem JPA, sem filtros
@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock PessoaCacheService servicoCache;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        PersonController controller = new PersonController(servicoCache);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private PessoaDto pessoaDto() {
        EnderecoDto endereco = new EnderecoDto("01310100", "Av Paulista", "Bela Vista", "Sao Paulo", "SP", null, "100");
        return new PessoaDto(UUID.randomUUID().toString(), "Maria Silva", "52998224725",
                "maria@example.com", LocalDate.of(1990, 1, 1), endereco, "msilvaa", LocalDateTime.now());
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

    // ── POST /api/v1/persons ──────────────────────────────────────────────────

    @Test
    void postDeveRetornar201QuandoCadastroValido() throws Exception {
        when(servicoCache.cadastrar(any())).thenReturn(pessoaDto());

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoValido())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("msilvaa"))
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Silva"));
    }

    @Test
    void postDeveRetornar400QuandoNomeFaltando() throws Exception {
        CommandCadastrarPessoa cmd = comandoValido();
        cmd.setNomeCompleto(null);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postDeveRetornar400QuandoEmailInvalido() throws Exception {
        CommandCadastrarPessoa cmd = comandoValido();
        cmd.setEmail("naoemail");

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postDeveRetornar400QuandoCepFaltando() throws Exception {
        CommandCadastrarPessoa cmd = comandoValido();
        cmd.setCep(null);

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postDeveRetornar422QuandoCpfJaCadastrado() throws Exception {
        when(servicoCache.cadastrar(any()))
                .thenThrow(new ExcecaoDominio("Já existe uma pessoa cadastrada com este CPF"));

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoValido())))
                .andExpect(status().isUnprocessableEntity());
    }

    // ── GET /api/v1/persons/{id} ──────────────────────────────────────────────

    @Test
    void getByIdDeveRetornar200ComPessoa() throws Exception {
        PessoaDto dto = pessoaDto();
        when(servicoCache.buscarPorId(any())).thenReturn(dto);

        mockMvc.perform(get("/api/v1/persons/{id}", dto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Silva"));
    }

    @Test
    void getByIdDeveRetornar422QuandoNaoEncontrado() throws Exception {
        when(servicoCache.buscarPorId(any()))
                .thenThrow(new ExcecaoDominio("Pessoa não encontrada"));

        mockMvc.perform(get("/api/v1/persons/{id}", UUID.randomUUID()))
                .andExpect(status().isUnprocessableEntity());
    }

    // ── GET /api/v1/persons ───────────────────────────────────────────────────

    @Test
    void listDeveRetornar200ComListaDePessoas() throws Exception {
        when(servicoCache.listarTodos(any())).thenReturn(List.of(pessoaDto(), pessoaDto()));

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listDeveRetornar200ComListaVazia() throws Exception {
        when(servicoCache.listarTodos(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
