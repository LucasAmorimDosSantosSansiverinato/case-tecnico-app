package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.command.RegisterPersonCommand;
import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.port.AddressServicePort;
import com.desafioTecnico.application.port.LoginGeneratorPort;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPersonCommandHandlerTest {

    @Mock PersonRepository personRepository;
    @Mock LoginGeneratorPort loginGenerator;
    @Mock AddressServicePort addressService;

    RegisterPersonCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RegisterPersonCommandHandler(personRepository, loginGenerator, addressService);
    }

    private RegisterPersonCommand validCommand() {
        return new RegisterPersonCommand(
                "Maria Silva",
                "529.982.247-25",
                "maria@example.com",
                LocalDate.of(1990, 1, 1),
                "01310100",
                null,
                "100"
        );
    }

    private Address stubAddress() {
        return Address.builder()
                .cep("01310100")
                .street("Avenida Paulista")
                .neighborhood("Bela Vista")
                .city("Sao Paulo")
                .state("SP")
                .build();
    }

    private Person savedPerson() {
        return Person.builder()
                .id(PersonId.generate())
                .fullName("Maria Silva")
                .document(Document.of("529.982.247-25"))
                .email("maria@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address(stubAddress())
                .login(Login.of("msilvaa"))
                .build();
    }

    @Test
    void shouldRegisterPersonSuccessfully() {
        when(personRepository.findByDocument("52998224725")).thenReturn(Optional.empty());
        when(personRepository.findByEmail("maria@example.com")).thenReturn(Optional.empty());
        when(addressService.findByCep("01310100")).thenReturn(stubAddress());
        when(personRepository.findAllLogins()).thenReturn(List.of());
        when(loginGenerator.generate(eq("Maria Silva"), anyList())).thenReturn(Login.of("msilvaa"));
        when(personRepository.save(any())).thenReturn(savedPerson());

        PersonResponse response = handler.handle(validCommand());

        assertNotNull(response);
        assertEquals("Maria Silva", response.getFullName());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void shouldThrowWhenDocumentAlreadyRegistered() {
        when(personRepository.findByDocument("52998224725")).thenReturn(Optional.of(savedPerson()));

        DomainException ex = assertThrows(DomainException.class, () -> handler.handle(validCommand()));
        assertTrue(ex.getMessage().contains("document"));
        verify(personRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmailAlreadyRegistered() {
        when(personRepository.findByDocument("52998224725")).thenReturn(Optional.empty());
        when(personRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(savedPerson()));

        DomainException ex = assertThrows(DomainException.class, () -> handler.handle(validCommand()));
        assertTrue(ex.getMessage().contains("email"));
        verify(personRepository, never()).save(any());
    }

    @Test
    void shouldPropagateExceptionFromAddressService() {
        when(personRepository.findByDocument(any())).thenReturn(Optional.empty());
        when(personRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(addressService.findByCep("01310100")).thenThrow(new DomainException("CEP not found"));

        assertThrows(DomainException.class, () -> handler.handle(validCommand()));
        verify(personRepository, never()).save(any());
    }

    @Test
    void shouldPassComplementAndNumberToAddress() {
        RegisterPersonCommand commandWithComplement = new RegisterPersonCommand(
                "Maria Silva", "529.982.247-25", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Apto 42", "200"
        );

        when(personRepository.findByDocument(any())).thenReturn(Optional.empty());
        when(personRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(addressService.findByCep("01310100")).thenReturn(stubAddress());
        when(personRepository.findAllLogins()).thenReturn(List.of());
        when(loginGenerator.generate(any(), any())).thenReturn(Login.of("msilvaa"));
        when(personRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PersonResponse response = handler.handle(commandWithComplement);
        assertNotNull(response);
    }
}
