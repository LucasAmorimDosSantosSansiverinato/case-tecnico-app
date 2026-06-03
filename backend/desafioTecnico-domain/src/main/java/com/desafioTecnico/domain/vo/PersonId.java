package com.desafioTecnico.domain.vo;

import java.util.Objects;
import java.util.UUID;

public final class PersonId {

    private final UUID value;

    private PersonId(UUID value) {
        this.value = Objects.requireNonNull(value, "PersonId cannot be null");
    }

    public static PersonId generate() {
        return new PersonId(UUID.randomUUID());
    }

    public static PersonId of(UUID value) {
        return new PersonId(value);
    }

    public static PersonId of(String value) {
        return new PersonId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonId other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
