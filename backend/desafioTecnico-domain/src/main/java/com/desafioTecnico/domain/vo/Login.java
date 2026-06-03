package com.desafioTecnico.domain.vo;

import com.desafioTecnico.domain.exception.DomainException;

import java.util.Objects;

public final class Login {

    private final String value;

    private Login(String value) {
        this.value = value;
    }

    public static Login of(String value) {
        validate(value);
        return new Login(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Login is required");
        }
        if (value.length() != 7) {
            throw new DomainException("Login must have exactly 7 characters");
        }
        if (!value.matches("[a-z]{7}")) {
            throw new DomainException("Login must contain only lowercase letters a-z");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
