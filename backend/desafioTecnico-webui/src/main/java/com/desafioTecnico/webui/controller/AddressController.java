package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.port.AddressServicePort;
import com.desafioTecnico.domain.vo.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final AddressServicePort addressService;

    public AddressController(AddressServicePort addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<?> findByCep(@PathVariable String cep) {
        String digits = cep.replaceAll("[^0-9]", "");
        log.info("[BACKEND] GET /api/v1/address/{} - buscando CEP", digits);

        Address address = addressService.findByCep(digits);

        return ResponseEntity.ok(Map.of(
                "cep", address.getCep(),
                "street", address.getStreet() != null ? address.getStreet() : "",
                "neighborhood", address.getNeighborhood() != null ? address.getNeighborhood() : "",
                "city", address.getCity(),
                "state", address.getState()
        ));
    }
}
