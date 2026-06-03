package com.desafioTecnico.domain.interface_;

import com.desafioTecnico.domain.entidade.Pessoa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepositorioPessoa {

    Pessoa salvar(Pessoa pessoa);

    Optional<Pessoa> buscarPorId(UUID id);

    Optional<Pessoa> buscarPorCpf(String cpf);

    Optional<Pessoa> buscarPorEmail(String email);

    boolean existePorLogin(String login);

    List<String> listarTodosLogins();

    Optional<Pessoa> buscarPorLogin(String login);

    List<Pessoa> listarTodos();
}
