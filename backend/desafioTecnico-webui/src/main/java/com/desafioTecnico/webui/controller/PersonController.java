package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.application.mediator.query.QueryPessoa;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.infra.data.cache.PessoaCacheService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PessoaCacheService servicoCache;

    public PersonController(PessoaCacheService servicoCache) {
        this.servicoCache = servicoCache;
    }

    @PostMapping
    public ResponseEntity<PessoaDto> cadastrar(@Valid @RequestBody CommandCadastrarPessoa comando) {
        log.info("[BACKEND] POST /api/v1/persons - cadastrando: {}", comando.getNomeCompleto());
        // Endereço é preenchido automaticamente via ViaCEP pelo HandlerCadastrarPessoa
        PessoaDto resposta = servicoCache.cadastrar(comando);
        log.info("[BACKEND] Cadastro realizado - login gerado: {}", resposta.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDto> buscarPorId(@PathVariable String id) {
        log.info("[BACKEND] GET /api/v1/persons/{}", id);
        PessoaDto resposta = servicoCache.buscarPorId(new QueryPessoa(id));
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<PessoaDto>> listarTodos() {
        log.info("[BACKEND] GET /api/v1/persons - listando todas");
        List<PessoaDto> resposta = servicoCache.listarTodos(new QueryTodasPessoas());
        return ResponseEntity.ok(resposta);
    }

    // Endpoint exclusivo para autenticação via BFF — protegido pelo ServiceTokenFilter
    @GetMapping("/login/{login}")
    public ResponseEntity<Pessoa> buscarPorLogin(@PathVariable String login) {
        log.info("[BACKEND] GET /api/v1/persons/login/{} - autenticação BFF", login);
        Pessoa pessoa = servicoCache.buscarPorLogin(login);
        return ResponseEntity.ok(pessoa);
    }

    // Tratamento de exceções de domínio (regras de negócio violadas)
    @ExceptionHandler(ExcecaoDominio.class)
    public ProblemDetail tratarExcecaoDominio(ExcecaoDominio ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problema.setTitle("Violação de Regra de Negócio");
        return problema;
    }

    // Tratamento de falhas de validação do request body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validação falhou");
        problema.setTitle("Requisição Inválida");
        problema.setProperty("erros", erros);
        return problema;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail tratarArgumentoInvalido(IllegalArgumentException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problema.setTitle("Argumento Inválido");
        return problema;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarGenerico(Exception ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado");
        problema.setTitle("Erro Interno do Servidor");
        return problema;
    }
}
