package com.desafioTecnico.infra.utils.address;

import com.desafioTecnico.application.port.AddressServicePort;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Address;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepAddressService implements AddressServicePort {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    private final RestTemplate restTemplate;

    public ViaCepAddressService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Address findByCep(String cep) {
        String cleanCep = cep.replaceAll("[^0-9]", "");
        if (cleanCep.length() != 8) {
            throw new DomainException("CEP must have 8 digits");
        }

        ViaCepClient response;
        try {
            response = restTemplate.getForObject(VIA_CEP_URL, ViaCepClient.class, cleanCep);
        } catch (Exception e) {
            throw new DomainException("Failed to fetch address from ViaCEP: " + e.getMessage());
        }

        if (response == null || Boolean.TRUE.equals(response.getError())) {
            throw new DomainException("CEP not found: " + cleanCep);
        }

        return Address.builder()
                .cep(cleanCep)
                .street(response.getStreet() != null ? response.getStreet() : "")
                .neighborhood(response.getNeighborhood())
                .city(response.getCity())
                .state(response.getState())
                .build();
    }
}
