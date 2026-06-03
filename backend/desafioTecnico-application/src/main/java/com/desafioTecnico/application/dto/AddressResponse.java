package com.desafioTecnico.application.dto;

import com.desafioTecnico.domain.vo.Address;

public class AddressResponse {

    public String cep;
    public String street;
    public String neighborhood;
    public String city;
    public String state;
    public String complement;
    public String number;

    public static AddressResponse from(Address address) {
        var r = new AddressResponse();
        r.cep          = address.getCep();
        r.street       = address.getStreet();
        r.neighborhood = address.getNeighborhood();
        r.city         = address.getCity();
        r.state        = address.getState();
        r.complement   = address.getComplement();
        r.number       = address.getNumber();
        return r;
    }
}
