package com.desafioTecnico.application.dto;

import com.desafioTecnico.domain.entity.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonResponse {

    private final String id;
    private final String fullName;
    private final String document;
    private final String email;
    private final LocalDate birthDate;
    private final AddressResponse address;
    private final String login;
    private final LocalDateTime createdAt;

    public PersonResponse(String id, String fullName, String document, String email,
                          LocalDate birthDate, AddressResponse address,
                          String login, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.login = login;
        this.createdAt = createdAt;
    }

    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId().toString(),
                person.getFullName(),
                person.getDocument().getFormatted(),
                person.getEmail(),
                person.getBirthDate(),
                AddressResponse.from(person.getAddress()),
                person.getLogin().getValue(),
                person.getCreatedAt()
        );
    }

    public String getId()             { return id; }
    public String getFullName()       { return fullName; }
    public String getDocument()       { return document; }
    public String getEmail()          { return email; }
    public LocalDate getBirthDate()   { return birthDate; }
    public AddressResponse getAddress(){ return address; }
    public String getLogin()          { return login; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}
