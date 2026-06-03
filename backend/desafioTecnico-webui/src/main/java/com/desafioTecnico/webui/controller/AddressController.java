package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.interface_.IPortaServicoEndereco;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final IPortaServicoEndereco servicoEndereco;

    public AddressController(IPortaServicoEndereco servicoEndereco) {
        this.servicoEndereco = servicoEndereco;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<?> buscarPorCep(@PathVariable String cep) {
        String digitos = cep.replaceAll("[^0-9]", "");
        log.info("[BACKEND] GET /api/v1/address/{} - buscando CEP", digitos);

        Map<String, String> dados = servicoEndereco.buscarPorCep(digitos);

        return ResponseEntity.ok(Map.of(
                "cep", dados.getOrDefault("cep", ""),
                "street", dados.getOrDefault("logradouro", ""),
                "neighborhood", dados.getOrDefault("bairro", ""),
                "city", dados.getOrDefault("cidade", ""),
                "state", dados.getOrDefault("estado", "")
        ));
    }
}
