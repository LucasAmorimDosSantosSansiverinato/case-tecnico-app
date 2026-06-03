package com.desafioTecnico.infra.data.adapter;

import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.vo.*;
import com.desafioTecnico.infra.data.entity.PersonJpaEntity;
import com.desafioTecnico.infra.data.repository.PersonJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryAdapterTest {

    @Mock PersonJpaRepository jpaRepository;

    PersonRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PersonRepositoryAdapter(jpaRepository);
    }

    private PersonJpaEntity buildEntity() {
        PersonJpaEntity entity = new PersonJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setFullName("Maria Silva");
        entity.setDocument("52998224725");
        entity.setEmail("maria@example.com");
        entity.setBirthDate(LocalDate.of(1990, 1, 1));
        entity.setCep("01310100");
        entity.setStreet("Avenida Paulista");
        entity.setNeighborhood("Bela Vista");
        entity.setCity("Sao Paulo");
        entity.setState("SP");
        entity.setLogin("msilvaa");
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    private Person buildDomainPerson() {
        return Person.builder()
                .id(PersonId.generate())
                .fullName("Maria Silva")
                .document(Document.of("529.982.247-25"))
                .email("maria@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address(Address.builder()
                        .cep("01310100")
                        .street("Avenida Paulista")
                        .city("Sao Paulo")
                        .state("SP")
                        .build())
                .login(Login.of("msilvaa"))
                .build();
    }

    @Test
    void shouldSaveAndReturnPerson() {
        PersonJpaEntity saved = buildEntity();
        when(jpaRepository.save(any(PersonJpaEntity.class))).thenReturn(saved);

        Person result = adapter.save(buildDomainPerson());

        assertNotNull(result);
        assertEquals("Maria Silva", result.getFullName());
        assertEquals("52998224725", result.getDocument().getValue());
        verify(jpaRepository).save(any(PersonJpaEntity.class));
    }

    @Test
    void shouldFindByIdWhenExists() {
        PersonJpaEntity entity = buildEntity();
        when(jpaRepository.findById(any(UUID.class))).thenReturn(Optional.of(entity));

        Optional<Person> result = adapter.findById(PersonId.generate());

        assertTrue(result.isPresent());
        assertEquals("Maria Silva", result.get().getFullName());
    }

    @Test
    void shouldReturnEmptyWhenIdNotFound() {
        when(jpaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<Person> result = adapter.findById(PersonId.generate());

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindByDocumentWhenExists() {
        PersonJpaEntity entity = buildEntity();
        when(jpaRepository.findByDocument("52998224725")).thenReturn(Optional.of(entity));

        Optional<Person> result = adapter.findByDocument("52998224725");

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenDocumentNotFound() {
        when(jpaRepository.findByDocument(any())).thenReturn(Optional.empty());

        Optional<Person> result = adapter.findByDocument("00000000000");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindByEmailWhenExists() {
        PersonJpaEntity entity = buildEntity();
        when(jpaRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(entity));

        Optional<Person> result = adapter.findByEmail("maria@example.com");

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        when(jpaRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertFalse(adapter.findByEmail("other@example.com").isPresent());
    }

    @Test
    void shouldCheckExistsByLogin() {
        when(jpaRepository.existsByLogin("msilva1")).thenReturn(true);
        when(jpaRepository.existsByLogin("xxxxxxx")).thenReturn(false);

        assertTrue(adapter.existsByLogin("msilva1"));
        assertFalse(adapter.existsByLogin("xxxxxxx"));
    }

    @Test
    void shouldReturnAllLogins() {
        when(jpaRepository.findAllLogins()).thenReturn(List.of("msilva1", "jlimaxyz"));

        List<String> logins = adapter.findAllLogins();

        assertEquals(2, logins.size());
    }

    @Test
    void shouldReturnAllPersons() {
        PersonJpaEntity e1 = buildEntity();
        PersonJpaEntity e2 = buildEntity();
        e2.setId(UUID.randomUUID());
        e2.setFullName("Joao Lima");
        e2.setDocument("98765432100");
        e2.setEmail("joao@example.com");
        e2.setLogin("jlimaab");

        when(jpaRepository.findAll()).thenReturn(List.of(e1, e2));

        List<Person> result = adapter.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldMapOptionalFieldsCorrectly() {
        PersonJpaEntity entity = buildEntity();
        entity.setComplement("Apto 42");
        entity.setNumber("200");
        when(jpaRepository.findById(any(UUID.class))).thenReturn(Optional.of(entity));

        Person person = adapter.findById(PersonId.generate()).get();

        assertEquals("Apto 42", person.getAddress().getComplement());
        assertEquals("200", person.getAddress().getNumber());
    }
}
