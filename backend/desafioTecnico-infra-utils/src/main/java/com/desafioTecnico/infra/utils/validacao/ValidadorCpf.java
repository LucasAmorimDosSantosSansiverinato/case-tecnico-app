package com.desafioTecnico.infra.utils.validacao;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;

public class ValidadorCpf {

    private ValidadorCpf() {}

    public static String validarENormalizar(String cpfBruto) {
        if (cpfBruto == null) throw new ExcecaoDominio("CPF é obrigatório");
        String digitos = cpfBruto.replaceAll("[^0-9]", "");
        if (digitos.length() != 11) {
            throw new ExcecaoDominio("CPF deve ter 11 dígitos");
        }
        if (digitos.chars().distinct().count() == 1) {
            throw new ExcecaoDominio("CPF inválido");
        }
        if (!verificarDigito(digitos, 9) || !verificarDigito(digitos, 10)) {
            throw new ExcecaoDominio("CPF inválido");
        }
        return digitos;
    }

    private static boolean verificarDigito(String digitos, int posicao) {
        int soma = 0;
        for (int i = 0; i < posicao; i++) {
            soma += (digitos.charAt(i) - '0') * (posicao + 1 - i);
        }
        int resto = (soma * 10) % 11;
        if (resto == 10) resto = 0;
        return resto == (digitos.charAt(posicao) - '0');
    }
}
