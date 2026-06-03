package com.desafioTecnico.domain;

import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person.Builder validBuilder() {
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
                .login(Login.of("msilvaa"));
    }

    @Test
    void shouldBuildValidPerson() {
        Person person = validBuilder().build();
        assertNotNull(person.getId());
        assertEquals("Maria Silva", person.getFullName());
        assertEquals("maria@example.com", person.getEmail());
        assertNotNull(person.getCreatedAt());
    }

    @Test
    void shouldRejectBlankFullName() {
        assertThrows(DomainException.class, () -> validBuilder().fullName("").build());
    }

    @Test
    void shouldRejectNullFullName() {
        assertThrows(DomainException.class, () -> validBuilder().fullName(null).build());
    }

    @Test
    void shouldRejectSingleWordFullName() {
        assertThrows(DomainException.class, () -> validBuilder().fullName("Maria").build());
    }

    @Test
    void shouldRejectFullNameWithNumbers() {
        assertThrows(DomainException.class, () -> validBuilder().fullName("Maria123 Silva").build());
    }

    @Test
    void shouldRejectFullNameWithSpecialChars() {
        assertThrows(DomainException.class, () -> validBuilder().fullName("Maria@ Silva").build());
    }

    @Test
    void shouldNormalizeFullNameSpaces() {
        Person person = validBuilder().fullName("  Maria   Silva  ").build();
        assertEquals("Maria Silva", person.getFullName());
    }

    @Test
    void shouldRejectBlankEmail() {
        assertThrows(DomainException.class, () -> validBuilder().email("").build());
    }

    @Test
    void shouldRejectNullEmail() {
        assertThrows(DomainException.class, () -> validBuilder().email(null).build());
    }

    @Test
    void shouldRejectInvalidEmailFormat() {
        assertThrows(DomainException.class, () -> validBuilder().email("notanemail").build());
        assertThrows(DomainException.class, () -> validBuilder().email("missing@domain").build());
    }

    @Test
    void shouldLowercaseEmail() {
        Person person = validBuilder().email("Maria@Example.COM").build();
        assertEquals("maria@example.com", person.getEmail());
    }

    @Test
    void shouldRejectNullBirthDate() {
        assertThrows(DomainException.class, () -> validBuilder().birthDate(null).build());
    }

    @Test
    void shouldRejectFutureBirthDate() {
        assertThrows(DomainException.class, () ->
                validBuilder().birthDate(LocalDate.now().plusDays(1)).build());
    }

    @Test
    void shouldAcceptTodayBirthDate() {
        assertDoesNotThrow(() -> validBuilder().birthDate(LocalDate.now()).build());
    }

    @Test
    void shouldRejectNullDocument() {
        assertThrows(RuntimeException.class, () -> validBuilder().document(null).build());
    }

    @Test
    void shouldRejectNullAddress() {
        assertThrows(RuntimeException.class, () -> validBuilder().address(null).build());
    }

    @Test
    void shouldRejectNullLogin() {
        assertThrows(RuntimeException.class, () -> validBuilder().login(null).build());
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(RuntimeException.class, () ->
                Person.builder()
                        .fullName("Maria Silva")
                        .document(Document.of("529.982.247-25"))
                        .email("maria@example.com")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .address(Address.builder().cep("01310100").street("Av Paulista").city("SP").state("SP").build())
                        .login(Login.of("msilvaa"))
                        .build());
    }

    @Test
    void shouldSupportCustomCreatedAt() {
        LocalDateTime fixedTime = LocalDateTime.of(2024, 6, 1, 12, 0);
        Person person = validBuilder().createdAt(fixedTime).build();
        assertEquals(fixedTime, person.getCreatedAt());
    }

    @Test
    void personsWithSameIdAreEqual() {
        PersonId id = PersonId.generate();
        Person p1 = validBuilder().id(id).build();
        Person p2 = validBuilder().id(id).build();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void personsWithDifferentIdAreNotEqual() {
        Person p1 = validBuilder().id(PersonId.generate()).build();
        Person p2 = validBuilder().id(PersonId.generate()).build();
        assertNotEquals(p1, p2);
    }

    @Test
    void shouldNotEqualNonPersonObject() {
        Person person = validBuilder().build();
        assertNotEquals(person, "not a person");
    }
}
