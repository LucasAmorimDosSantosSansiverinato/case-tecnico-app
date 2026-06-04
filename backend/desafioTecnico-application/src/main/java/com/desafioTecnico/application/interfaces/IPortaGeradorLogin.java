package com.desafioTecnico.application.interfaces;

import java.util.List;
// Porta de saída para geração de login único a partir do nome completo
public interface IPortaGeradorLogin {
    String gerar(String nomeCompleto, List<String> loginsExistentes);
}
