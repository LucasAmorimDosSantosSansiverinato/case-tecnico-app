package com.desafioTecnico.domain;

import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address.Builder validBuilder() {
        return Address.builder()
                .cep("01310100")
                .street("Avenida Paulista")
                .city("Sao Paulo")
                .state("SP");
    }

    @Test
    void shouldBuildValidAddress() {
        Address address = validBuilder().build();
        assertEquals("01310100", address.getCep());
        assertEquals("Avenida Paulista", address.getStreet());
        assertEquals("Sao Paulo", address.getCity());
        assertEquals("SP", address.getState());
    }

    @Test
    void shouldStripFormattingFromCep() {
        Address address = validBuilder().cep("01310-100").build();
        assertEquals("01310100", address.getCep());
    }

    @Test
    void shouldRejectNullCep() {
        assertThrows(DomainException.class, () -> validBuilder().cep(null).build());
    }

    @Test
    void shouldRejectCepWithWrongLength() {
        assertThrows(DomainException.class, () -> validBuilder().cep("0131010").build());
        assertThrows(DomainException.class, () -> validBuilder().cep("013101000").build());
    }

    @Test
    void shouldRejectNullStreet() {
        assertThrows(RuntimeException.class, () -> validBuilder().street(null).build());
    }

    @Test
    void shouldRejectNullCity() {
        assertThrows(RuntimeException.class, () -> validBuilder().city(null).build());
    }

    @Test
    void shouldRejectNullState() {
        assertThrows(RuntimeException.class, () -> validBuilder().state(null).build());
    }

    @Test
    void shouldAcceptOptionalFields() {
        Address address = validBuilder()
                .neighborhood("Bela Vista")
                .complement("Apto 101")
                .number("1578")
                .build();
        assertEquals("Bela Vista", address.getNeighborhood());
        assertEquals("Apto 101", address.getComplement());
        assertEquals("1578", address.getNumber());
    }

    @Test
    void addressesWithSameCepStreetNumberAreEqual() {
        Address a1 = validBuilder().number("100").build();
        Address a2 = validBuilder().number("100").build();
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void addressesWithDifferentNumberAreNotEqual() {
        Address a1 = validBuilder().number("100").build();
        Address a2 = validBuilder().number("200").build();
        assertNotEquals(a1, a2);
    }

    @Test
    void shouldNotEqualNonAddressObject() {
        Address address = validBuilder().build();
        assertNotEquals(address, "string");
    }
}
