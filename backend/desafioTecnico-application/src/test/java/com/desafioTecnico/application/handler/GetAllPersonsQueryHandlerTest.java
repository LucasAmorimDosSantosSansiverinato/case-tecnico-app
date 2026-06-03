package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.query.GetAllPersonsQuery;
import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.repository.PersonRepository;
import com.desafioTecnico.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllPersonsQueryHandlerTest {

    @Mock PersonRepository personRepository;

    GetAllPersonsQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllPersonsQueryHandler(personRepository);
    }

    private Person buildPerson(String name, String cpf, String email, String loginVal) {
        return Person.builder()
                .id(PersonId.generate())
                .fullName(name)
                .document(Document.of(cpf))
                .email(email)
                .birthDate(LocalDate.of(1990, 1, 1))
                .address(Address.builder()
                        .cep("01310100")
                        .street("Av Paulista")
                        .city("Sao Paulo")
                        .state("SP")
                        .build())
                .login(Login.of(loginVal))
                .build();
    }

    @Test
    void shouldReturnEmptyListWhenNoPersons() {
        when(personRepository.findAll()).thenReturn(List.of());

        List<PersonResponse> result = handler.handle(new GetAllPersonsQuery());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllPersons() {
        Person p1 = buildPerson("Maria Silva", "529.982.247-25", "maria@example.com", "msilvaa");
        Person p2 = buildPerson("Joao Lima", "987.654.321-00", "joao@example.com", "jlimabb");

        when(personRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PersonResponse> result = handler.handle(new GetAllPersonsQuery());

        assertEquals(2, result.size());
        assertEquals("Maria Silva", result.get(0).getFullName());
        assertEquals("Joao Lima", result.get(1).getFullName());
    }
}
