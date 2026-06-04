package com.desafioTecnico.application.interfaces;

import java.util.Map;

// Porta de saída para busca de endereço por CEP via serviço externo (ViaCEP)
public interface IPortaServicoEndereco {

    Map<String, String> buscarPorCep(String cep);
}
