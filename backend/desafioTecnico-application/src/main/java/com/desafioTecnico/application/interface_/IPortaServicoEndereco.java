package com.desafioTecnico.application.interface_;

import java.util.Map;

public interface IPortaServicoEndereco {
    Map<String, String> buscarPorCep(String cep);
}
