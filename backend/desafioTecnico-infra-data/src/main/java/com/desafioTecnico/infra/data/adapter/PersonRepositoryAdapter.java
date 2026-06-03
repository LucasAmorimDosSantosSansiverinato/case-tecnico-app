package com.desafioTecnico.infra.data.adapter;

import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.repository.PersonRepository;
import com.desafioTecnico.domain.vo.*;
import com.desafioTecnico.infra.data.entity.PersonJpaEntity;
import com.desafioTecnico.infra.data.repository.PersonJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonRepositoryAdapter implements PersonRepository {

    private final PersonJpaRepository jpaRepository;

    public PersonRepositoryAdapter(PersonJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Person save(Person person) {
        PersonJpaEntity entity = toEntity(person);
        PersonJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Person> findById(PersonId id) {
        return jpaRepository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<Person> findByDocument(String document) {
        return jpaRepository.findByDocument(document).map(this::toDomain);
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByLogin(String login) {
        return jpaRepository.existsByLogin(login);
    }

    @Override
    public List<String> findAllLogins() {
        return jpaRepository.findAllLogins();
    }

    @Override
    public List<Person> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private PersonJpaEntity toEntity(Person person) {
        PersonJpaEntity entity = new PersonJpaEntity();
        entity.setId(person.getId().getValue());
        entity.setFullName(person.getFullName());
        entity.setDocument(person.getDocument().getValue());
        entity.setEmail(person.getEmail());
        entity.setBirthDate(person.getBirthDate());
        entity.setCep(person.getAddress().getCep());
        entity.setStreet(person.getAddress().getStreet());
        entity.setNeighborhood(person.getAddress().getNeighborhood());
        entity.setCity(person.getAddress().getCity());
        entity.setState(person.getAddress().getState());
        entity.setComplement(person.getAddress().getComplement());
        entity.setNumber(person.getAddress().getNumber());
        entity.setLogin(person.getLogin().getValue());
        entity.setCreatedAt(person.getCreatedAt());
        return entity;
    }

    private Person toDomain(PersonJpaEntity entity) {
        Address address = Address.builder()
                .cep(entity.getCep())
                .street(entity.getStreet())
                .neighborhood(entity.getNeighborhood())
                .city(entity.getCity())
                .state(entity.getState())
                .complement(entity.getComplement())
                .number(entity.getNumber())
                .build();

        return Person.builder()
                .id(PersonId.of(entity.getId()))
                .fullName(entity.getFullName())
                .document(Document.restore(entity.getDocument()))
                .email(entity.getEmail())
                .birthDate(entity.getBirthDate())
                .address(address)
                .login(Login.of(entity.getLogin()))
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
