package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.query.GetPersonByIdQuery;
import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.repository.PersonRepository;
import com.desafioTecnico.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPersonByIdQueryHandlerTest {

    @Mock PersonRepository personRepository;

    GetPersonByIdQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetPersonByIdQueryHandler(personRepository);
    }

    private Person buildPerson(PersonId id) {
        return Person.builder()
                .id(id)
                .fullName("Maria Silva")
                .document(Document.of("529.982.247-25"))
                .email("maria@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address(Address.builder()
                        .cep("01310100")
                        .street("Av Paulista")
                        .city("Sao Paulo")
                        .state("SP")
                        .build())
                .login(Login.of("msilvaa"))
                .build();
    }

    @Test
    void shouldReturnPersonWhenFound() {
        PersonId id = PersonId.generate();
        when(personRepository.findById(any(PersonId.class))).thenReturn(Optional.of(buildPerson(id)));

        PersonResponse response = handler.handle(new GetPersonByIdQuery(id.toString()));

        assertEquals("Maria Silva", response.getFullName());
        assertEquals(id.toString(), response.getId());
    }

    @Test
    void shouldThrowDomainExceptionWhenNotFound() {
        when(personRepository.findById(any(PersonId.class))).thenReturn(Optional.empty());

        String uuid = UUID.randomUUID().toString();
        DomainException ex = assertThrows(DomainException.class,
                () -> handler.handle(new GetPersonByIdQuery(uuid)));

        assertTrue(ex.getMessage().contains("not found"));
    }
}
