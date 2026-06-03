package com.desafioTecnico.domain.vo;

import com.desafioTecnico.domain.exception.DomainException;

import java.util.Objects;

public final class Document {

    private final String value;

    private Document(String value) {
        this.value = value;
    }

    public static Document of(String raw) {
        String digits = normalize(raw);
        validate(digits);
        return new Document(digits);
    }

    private static String normalize(String raw) {
        if (raw == null) throw new DomainException("Document is required");
        return raw.replaceAll("[^0-9]", "");
    }

    private static void validate(String digits) {
        if (digits.length() != 11) {
            throw new DomainException("CPF must have 11 digits");
        }
        if (digits.chars().distinct().count() == 1) {
            throw new DomainException("Invalid CPF");
        }
        if (!checkDigit(digits, 9) || !checkDigit(digits, 10)) {
            throw new DomainException("Invalid CPF");
        }
    }

    private static boolean checkDigit(String digits, int position) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += (digits.charAt(i) - '0') * (position + 1 - i);
        }
        int remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        return remainder == (digits.charAt(position) - '0');
    }

    public String getValue() {
        return value;
    }

    public String getFormatted() {
        return value.substring(0, 3) + "." + value.substring(3, 6) + "." +
               value.substring(6, 9) + "-" + value.substring(9, 11);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
