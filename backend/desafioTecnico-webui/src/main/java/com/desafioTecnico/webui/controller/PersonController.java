package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorId;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorLogin;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import com.desafioTecnico.infra.data.cache.PessoaCacheService;
import com.desafioTecnico.webui.dto.RequisicaoCadastro;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PessoaCacheService servicoCache;

    public PersonController(PessoaCacheService servicoCache) {
        this.servicoCache = servicoCache;
    }

    @PostMapping
    public ResponseEntity<PessoaDto> cadastrar(@Valid @RequestBody RequisicaoCadastro requisicao) {
        log.info("[BACKEND] POST /api/v1/persons - cadastrando: {}", requisicao.getNomeCompleto());
        CommandCadastrarPessoa comando = new CommandCadastrarPessoa(
                requisicao.getNomeCompleto(),
                requisicao.getCpf(),
                requisicao.getEmail(),
                requisicao.getDataNascimento(),
                requisicao.getCep(),
                requisicao.getComplemento(),
                requisicao.getNumero()
        );
        PessoaDto resposta = servicoCache.cadastrar(comando);
        log.info("[BACKEND] Cadastro realizado com sucesso - login gerado: {}", resposta.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDto> buscarPorId(@PathVariable String id) {
        log.info("[BACKEND] GET /api/v1/persons/{}", id);
        PessoaDto resposta = servicoCache.buscarPorId(new QueryPessoaPorId(id));
        log.info("[BACKEND] Pessoa encontrada: {}", resposta.getNomeCompleto());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<PessoaDto> buscarPorLogin(@PathVariable String login) {
        log.info("[BACKEND] GET /api/v1/persons/login/{}", login);
        PessoaDto resposta = servicoCache.buscarPorLogin(new QueryPessoaPorLogin(login));
        log.info("[BACKEND] Login encontrado: {}", resposta.getNomeCompleto());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<PessoaDto>> listarTodos() {
        log.info("[BACKEND] GET /api/v1/persons - buscando todas as pessoas");
        List<PessoaDto> resposta = servicoCache.listarTodos(new QueryTodasPessoas());
        log.info("[BACKEND] Retornando {} pessoas do banco", resposta.size());
        return ResponseEntity.ok(resposta);
    }
}
