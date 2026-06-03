package com.desafioTecnico.infra.data.contexto;

import com.desafioTecnico.infra.data.entidade.EntidadePessoaJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContextoPessoaJpa extends JpaRepository<EntidadePessoaJpa, UUID> {

    Optional<EntidadePessoaJpa> findByCpf(String cpf);

    Optional<EntidadePessoaJpa> findByEmail(String email);

    Optional<EntidadePessoaJpa> findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("SELECT p.login FROM EntidadePessoaJpa p")
    List<String> findAllLogins();
}
