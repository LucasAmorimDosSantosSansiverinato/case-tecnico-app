package com.desafioTecnico.infra.utils.validacao;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;

public class ValidadorEndereco {

    private ValidadorEndereco() {}

    public static String validarENormalizarCep(String cep) {
        if (cep == null) throw new ExcecaoDominio("CEP é obrigatório");
        String digitos = cep.replaceAll("[^0-9]", "");
        if (digitos.length() != 8) throw new ExcecaoDominio("CEP deve ter 8 dígitos");
        return digitos;
    }
}
