package com.desafioTecnico.application.interface_;

import java.util.List;

public interface IPortaGeradorLogin {
    String gerar(String nomeCompleto, List<String> loginsExistentes);
}
