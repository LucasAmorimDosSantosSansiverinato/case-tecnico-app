package com.desafioTecnico.infra.data.cache;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerTodasPessoas;
import com.desafioTecnico.application.mediator.query.QueryPessoa;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Service
public class PessoaCacheService {

    private static final Logger log = LoggerFactory.getLogger(PessoaCacheService.class);

    // Garante que apenas um thread por vez escreve no cache, evitando duplicidade
    private final Semaphore cacheLock = new Semaphore(1, true);

    private final ConcurrentHashMap<UUID, PessoaDto> cachePorId = new ConcurrentHashMap<>();
    private volatile List<PessoaDto> cacheTodos = null;

    private final HandlerCadastrarPessoa manipuladorCadastrar;
    private final HandlerQueryPessoa manipuladorQuery;
    private final HandlerTodasPessoas manipuladorTodos;
    // Acesso direto ao repositório exclusivamente para autenticação por login
    private final IRepositorioPessoa repositorioPessoa;

    public PessoaCacheService(
            HandlerCadastrarPessoa manipuladorCadastrar,
            HandlerQueryPessoa manipuladorQuery,
            HandlerTodasPessoas manipuladorTodos,
            IRepositorioPessoa repositorioPessoa
    ) {
        this.manipuladorCadastrar = manipuladorCadastrar;
        this.manipuladorQuery = manipuladorQuery;
        this.manipuladorTodos = manipuladorTodos;
        this.repositorioPessoa = repositorioPessoa;
    }

    // Busca por login — usado apenas para autenticação, sem cache intencional
    public Pessoa buscarPorLogin(String login) {
        return repositorioPessoa.buscarPorLogin(login)
                .orElseThrow(() -> new ExcecaoDominio("Login não encontrado: " + login));
    }

    public PessoaDto cadastrar(CommandCadastrarPessoa comando) {
        PessoaDto resultado = manipuladorCadastrar.handle(comando);

        // Adiciona o novo cadastro aos caches existentes sem remover os anteriores
        inserirNoCacheComLock(resultado);
        return resultado;
    }

    public PessoaDto buscarPorId(QueryPessoa consulta) {
        UUID id = UUID.fromString(consulta.getId());
        PessoaDto emCache = cachePorId.get(id);
        if (emCache != null) {
            log.info("[CACHE] Hit por id={}", consulta.getId());
            return emCache;
        }

        log.info("[CACHE] Miss por id={} — consultando banco", consulta.getId());
        PessoaDto resultado = manipuladorQuery.handle(consulta);

        try {
            cacheLock.acquire();
            try {
                cachePorId.put(id, resultado);
            } finally {
                cacheLock.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[CACHE] Interrompido ao inserir por id");
        }

        return resultado;
    }

    public List<PessoaDto> listarTodos(QueryTodasPessoas consulta) {
        if (cacheTodos != null) {
            log.info("[CACHE] Hit para lista de pessoas");
            return cacheTodos;
        }

        log.info("[CACHE] Miss para lista de pessoas — consultando banco");
        List<PessoaDto> resultado = manipuladorTodos.handle(consulta);

        try {
            cacheLock.acquire();
            try {
                // Double-check para evitar sobrescrever cache já populado por outro thread
                if (cacheTodos == null) {
                    cacheTodos = resultado;
                }
            } finally {
                cacheLock.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[CACHE] Interrompido ao inserir lista");
        }

        return resultado;
    }

    private void inserirNoCacheComLock(PessoaDto pessoa) {
        try {
            cacheLock.acquire();
            try {
                cachePorId.put(UUID.fromString(pessoa.getId()), pessoa);

                // Se a lista já estava em cache, soma o novo registro sem descartar os anteriores
                if (cacheTodos != null) {
                    List<PessoaDto> atualizada = new java.util.ArrayList<>(cacheTodos);
                    atualizada.add(pessoa);
                    cacheTodos = atualizada;
                }
            } finally {
                cacheLock.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[CACHE] Interrompido ao inserir após cadastro");
        }
    }
}
