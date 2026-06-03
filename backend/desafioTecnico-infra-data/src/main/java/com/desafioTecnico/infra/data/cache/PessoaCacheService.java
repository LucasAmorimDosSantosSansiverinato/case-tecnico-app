package com.desafioTecnico.infra.data.cache;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPorId;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPorLogin;
import com.desafioTecnico.application.mediator.handler.HandlerTodasPessoas;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorId;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorLogin;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaCacheService {

    private static final Logger log = LoggerFactory.getLogger(PessoaCacheService.class);

    private final HandlerCadastrarPessoa manipuladorCadastrar;
    private final HandlerQueryPorId manipuladorPorId;
    private final HandlerQueryPorLogin manipuladorPorLogin;
    private final HandlerTodasPessoas manipuladorTodos;

    public PessoaCacheService(
            HandlerCadastrarPessoa manipuladorCadastrar,
            HandlerQueryPorId manipuladorPorId,
            HandlerQueryPorLogin manipuladorPorLogin,
            HandlerTodasPessoas manipuladorTodos
    ) {
        this.manipuladorCadastrar = manipuladorCadastrar;
        this.manipuladorPorId = manipuladorPorId;
        this.manipuladorPorLogin = manipuladorPorLogin;
        this.manipuladorTodos = manipuladorTodos;
    }

    @Caching(evict = {
        @CacheEvict(value = "persons-all", allEntries = true),
        @CacheEvict(value = "persons-by-id", allEntries = true)
    })
    public PessoaDto cadastrar(CommandCadastrarPessoa comando) {
        log.info("[CACHE] Evictando cache de pessoas após novo cadastro");
        return manipuladorCadastrar.handle(comando);
    }

    @Cacheable(value = "persons-by-id", key = "#consulta.id()")
    public PessoaDto buscarPorId(QueryPessoaPorId consulta) {
        log.info("[CACHE] Cache miss para pessoa id={}", consulta.id());
        return manipuladorPorId.handle(consulta);
    }

    @Cacheable(value = "persons-by-login", key = "#consulta.login()")
    public PessoaDto buscarPorLogin(QueryPessoaPorLogin consulta) {
        log.info("[CACHE] Cache miss para login={}", consulta.login());
        return manipuladorPorLogin.handle(consulta);
    }

    @Cacheable(value = "persons-all")
    public List<PessoaDto> listarTodos(QueryTodasPessoas consulta) {
        log.info("[CACHE] Cache miss para lista de pessoas — consultando banco");
        return manipuladorTodos.handle(consulta);
    }
}
