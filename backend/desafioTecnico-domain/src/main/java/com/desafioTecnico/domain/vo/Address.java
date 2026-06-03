package com.desafioTecnico.domain.vo;

import com.desafioTecnico.domain.exception.DomainException;

import java.util.Objects;

public final class Address {

    private final String cep;
    private final String street;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String complement;
    private final String number;

    private Address(Builder builder) {
        this.cep = builder.cep;
        this.street = builder.street;
        this.neighborhood = builder.neighborhood;
        this.city = builder.city;
        this.state = builder.state;
        this.complement = builder.complement;
        this.number = builder.number;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCep() { return cep; }
    public String getStreet() { return street; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getComplement() { return complement; }
    public String getNumber() { return number; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address other)) return false;
        return Objects.equals(cep, other.cep) &&
               Objects.equals(street, other.street) &&
               Objects.equals(number, other.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cep, street, number);
    }

    public static class Builder {
        private String cep;
        private String street;
        private String neighborhood;
        private String city;
        private String state;
        private String complement;
        private String number;

        public Builder cep(String cep) {
            this.cep = validateCep(cep);
            return this;
        }

        public Builder street(String street) {
            this.street = Objects.requireNonNull(street, "Street is required");
            return this;
        }

        public Builder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public Builder city(String city) {
            this.city = Objects.requireNonNull(city, "City is required");
            return this;
        }

        public Builder state(String state) {
            this.state = Objects.requireNonNull(state, "State is required");
            return this;
        }

        public Builder complement(String complement) {
            this.complement = complement;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Address build() {
            return new Address(this);
        }

        private String validateCep(String cep) {
            if (cep == null) throw new DomainException("CEP is required");
            String digits = cep.replaceAll("[^0-9]", "");
            if (digits.length() != 8) throw new DomainException("CEP must have 8 digits");
            return digits;
        }
    }
}
