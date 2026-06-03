package com.desafioTecnico.application.dto;

import com.desafioTecnico.domain.vo.Address;

public class AddressResponse {

    private final String cep;
    private final String street;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String complement;
    private final String number;

    public AddressResponse(String cep, String street, String neighborhood,
                           String city, String state,
                           String complement, String number) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.complement = complement;
        this.number = number;
    }

    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getCep(),
                address.getStreet(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getComplement(),
                address.getNumber()
        );
    }

    public String getCep()          { return cep; }
    public String getStreet()       { return street; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity()         { return city; }
    public String getState()        { return state; }
    public String getComplement()   { return complement; }
    public String getNumber()       { return number; }
}
