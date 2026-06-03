package com.desafioTecnico.infra.utils.endereco;

import com.desafioTecnico.application.interface_.IPortaServicoEndereco;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EnderecoViaCepService implements IPortaServicoEndereco {

    private static final String URL_VIA_CEP = "https://viacep.com.br/ws/{cep}/json/";

    private final RestTemplate restTemplate;

    public EnderecoViaCepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, String> buscarPorCep(String cep) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8) {
            throw new ExcecaoDominio("CEP deve ter 8 dígitos");
        }

        ClienteViaCep resposta;
        try {
            resposta = restTemplate.getForObject(URL_VIA_CEP, ClienteViaCep.class, cepLimpo);
        } catch (Exception e) {
            throw new ExcecaoDominio("Falha ao consultar ViaCEP: " + e.getMessage());
        }

        if (resposta == null || Boolean.TRUE.equals(resposta.getErro())) {
            throw new ExcecaoDominio("CEP não encontrado: " + cepLimpo);
        }

        return Map.of(
            "cep",        cepLimpo,
            "logradouro", resposta.getLogradouro() != null ? resposta.getLogradouro() : "",
            "bairro",     resposta.getBairro()     != null ? resposta.getBairro()     : "",
            "cidade",     resposta.getCidade()     != null ? resposta.getCidade()     : "",
            "estado",     resposta.getEstado()     != null ? resposta.getEstado()     : ""
        );
    }
}
