package com.desafioTecnico.application.interfaces;

public interface IPortaValidadorCpf {

    // Remove formatação, valida dígitos e retorna apenas os 11 números
    String validarENormalizar(String cpf);
}
