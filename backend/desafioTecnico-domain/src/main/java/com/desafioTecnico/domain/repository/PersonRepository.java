package com.desafioTecnico.domain.repository;

import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.vo.PersonId;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(PersonId id);

    Optional<Person> findByDocument(String document);

    Optional<Person> findByEmail(String email);

    boolean existsByLogin(String login);

    List<String> findAllLogins();

    List<Person> findAll();
}
