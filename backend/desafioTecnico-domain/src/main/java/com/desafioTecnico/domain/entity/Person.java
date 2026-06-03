package com.desafioTecnico.domain.entity;

import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Address;
import com.desafioTecnico.domain.vo.Document;
import com.desafioTecnico.domain.vo.Login;
import com.desafioTecnico.domain.vo.PersonId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Person {

    private final PersonId id;
    private final String fullName;
    private final Document document;
    private final String email;
    private final LocalDate birthDate;
    private final Address address;
    private final Login login;
    private final LocalDateTime createdAt;

    private Person(Builder builder) {
        this.id = builder.id;
        this.fullName = builder.fullName;
        this.document = builder.document;
        this.email = builder.email;
        this.birthDate = builder.birthDate;
        this.address = builder.address;
        this.login = builder.login;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public PersonId getId() { return id; }
    public String getFullName() { return fullName; }
    public Document getDocument() { return document; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public Address getAddress() { return address; }
    public Login getLogin() { return login; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private PersonId id;
        private String fullName;
        private Document document;
        private String email;
        private LocalDate birthDate;
        private Address address;
        private Login login;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder id(PersonId id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = validateFullName(fullName);
            return this;
        }

        public Builder document(Document document) {
            this.document = Objects.requireNonNull(document, "Document is required");
            return this;
        }

        public Builder email(String email) {
            this.email = validateEmail(email);
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = validateBirthDate(birthDate);
            return this;
        }

        public Builder address(Address address) {
            this.address = Objects.requireNonNull(address, "Address is required");
            return this;
        }

        public Builder login(Login login) {
            this.login = Objects.requireNonNull(login, "Login is required");
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Person build() {
            Objects.requireNonNull(id, "Id is required");
            Objects.requireNonNull(login, "Login is required");
            return new Person(this);
        }

        private String validateFullName(String name) {
            if (name == null || name.isBlank()) {
                throw new DomainException("Full name is required");
            }
            String trimmed = name.trim().replaceAll("\\s+", " ");
            // Only letters and spaces (no accents, special chars, digits)
            if (!trimmed.matches("[a-zA-Z ]+")) {
                throw new DomainException("Full name must contain only letters and spaces (no accents or special characters)");
            }
            if (trimmed.split(" ").length < 2) {
                throw new DomainException("Full name must contain at least first and last name");
            }
            return trimmed;
        }

        private String validateEmail(String email) {
            if (email == null || email.isBlank()) {
                throw new DomainException("Email is required");
            }
            if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                throw new DomainException("Invalid email format");
            }
            return email.toLowerCase().trim();
        }

        private LocalDate validateBirthDate(LocalDate date) {
            if (date == null) {
                throw new DomainException("Birth date is required");
            }
            if (date.isAfter(LocalDate.now())) {
                throw new DomainException("Birth date cannot be in the future");
            }
            return date;
        }
    }
}
