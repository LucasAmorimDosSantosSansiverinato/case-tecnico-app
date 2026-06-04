package com.desafioTecnico.infra.data.context;

import com.desafioTecnico.domain.entidade.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Contexto JPA — ponto único de acesso ao banco para leituras e escritas de Pessoa
@Repository
public interface ContextPessoaJpa extends JpaRepository<Pessoa, UUID> {

    Optional<Pessoa> findByCpf(String cpf);

    Optional<Pessoa> findByEmail(String email);

    Optional<Pessoa> findByLogin(String login);

    boolean existsByLogin(String login);

    // Retorna apenas os logins para evitar carregar entidades desnecessárias na geração de login
    @Query("SELECT p.login FROM Pessoa p")
    List<String> findAllLogins();
}
