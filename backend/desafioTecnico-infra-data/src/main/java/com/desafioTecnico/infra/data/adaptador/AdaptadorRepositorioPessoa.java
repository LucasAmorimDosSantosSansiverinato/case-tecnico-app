package com.desafioTecnico.infra.data.adaptador;

import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import com.desafioTecnico.infra.data.context.ContextPessoaJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Adaptador que implementa a interface de domínio delegando ao contexto JPA
@Component
public class AdaptadorRepositorioPessoa implements IRepositorioPessoa {

    // Toda operação de banco (leitura ou escrita) passa pelo contexto
    private final ContextPessoaJpa contexto;

    public AdaptadorRepositorioPessoa(ContextPessoaJpa contexto) {
        this.contexto = contexto;
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        return contexto.save(pessoa);
    }

    @Override
    public Optional<Pessoa> buscarPorId(UUID id) {
        return contexto.findById(id);
    }

    @Override
    public Optional<Pessoa> buscarPorCpf(String cpf) {
        return contexto.findByCpf(cpf);
    }

    @Override
    public Optional<Pessoa> buscarPorEmail(String email) {
        return contexto.findByEmail(email);
    }

    @Override
    public Optional<Pessoa> buscarPorLogin(String login) {
        return contexto.findByLogin(login);
    }

    @Override
    public boolean existePorLogin(String login) {
        return contexto.existsByLogin(login);
    }

    @Override
    public List<String> listarTodosLogins() {
        return contexto.findAllLogins();
    }

    @Override
    public List<Pessoa> listarTodos() {
        return contexto.findAll();
    }
}
