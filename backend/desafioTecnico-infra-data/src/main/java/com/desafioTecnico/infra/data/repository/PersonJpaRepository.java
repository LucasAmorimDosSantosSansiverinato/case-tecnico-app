package com.desafioTecnico.infra.data.repository;

import com.desafioTecnico.infra.data.entity.PersonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, UUID> {

    Optional<PersonJpaEntity> findByDocument(String document);

    Optional<PersonJpaEntity> findByEmail(String email);

    boolean existsByLogin(String login);

    @Query("SELECT p.login FROM PersonJpaEntity p")
    List<String> findAllLogins();
}
