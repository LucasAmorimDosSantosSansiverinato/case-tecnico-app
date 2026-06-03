package com.desafioTecnico.infra.data.adaptador;

import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import com.desafioTecnico.infra.data.contexto.ContextoPessoaJpa;
import com.desafioTecnico.infra.data.entidade.EntidadePessoaJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AdaptadorRepositorioPessoa implements IRepositorioPessoa {

    private final ContextoPessoaJpa contexto;

    public AdaptadorRepositorioPessoa(ContextoPessoaJpa contexto) {
        this.contexto = contexto;
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        EntidadePessoaJpa entidade = paraEntidade(pessoa);
        EntidadePessoaJpa salva = contexto.save(entidade);
        return paraDominio(salva);
    }

    @Override
    public Optional<Pessoa> buscarPorId(UUID id) {
        return contexto.findById(id).map(this::paraDominio);
    }

    @Override
    public Optional<Pessoa> buscarPorCpf(String cpf) {
        return contexto.findByCpf(cpf).map(this::paraDominio);
    }

    @Override
    public Optional<Pessoa> buscarPorEmail(String email) {
        return contexto.findByEmail(email).map(this::paraDominio);
    }

    @Override
    public Optional<Pessoa> buscarPorLogin(String login) {
        return contexto.findByLogin(login).map(this::paraDominio);
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
        return contexto.findAll().stream().map(this::paraDominio).toList();
    }

    private EntidadePessoaJpa paraEntidade(Pessoa pessoa) {
        EntidadePessoaJpa entidade = new EntidadePessoaJpa();
        entidade.setId(pessoa.getId());
        entidade.setNomeCompleto(pessoa.getNomeCompleto());
        entidade.setCpf(pessoa.getCpf());
        entidade.setEmail(pessoa.getEmail());
        entidade.setDataNascimento(pessoa.getDataNascimento());
        entidade.setCep(pessoa.getCep());
        entidade.setLogradouro(pessoa.getLogradouro());
        entidade.setBairro(pessoa.getBairro());
        entidade.setCidade(pessoa.getCidade());
        entidade.setEstado(pessoa.getEstado());
        entidade.setComplemento(pessoa.getComplemento());
        entidade.setNumero(pessoa.getNumero());
        entidade.setLogin(pessoa.getLogin());
        entidade.setCriadoEm(pessoa.getCriadoEm());
        return entidade;
    }

    private Pessoa paraDominio(EntidadePessoaJpa entidade) {
        return Pessoa.builder()
                .id(entidade.getId())
                .nomeCompleto(entidade.getNomeCompleto())
                .cpf(entidade.getCpf())
                .email(entidade.getEmail())
                .dataNascimento(entidade.getDataNascimento())
                .cep(entidade.getCep())
                .logradouro(entidade.getLogradouro())
                .bairro(entidade.getBairro())
                .cidade(entidade.getCidade())
                .estado(entidade.getEstado())
                .complemento(entidade.getComplemento())
                .numero(entidade.getNumero())
                .login(entidade.getLogin())
                .criadoEm(entidade.getCriadoEm())
                .build();
    }
}
