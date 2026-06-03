package com.desafioTecnico.infra.utils.validacao;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;

public class ValidadorLogin {

    private ValidadorLogin() {}

    public static void validar(String login) {
        if (login == null || login.isBlank()) {
            throw new ExcecaoDominio("Login é obrigatório");
        }
        if (login.length() != 7) {
            throw new ExcecaoDominio("Login deve ter exatamente 7 caracteres");
        }
        if (!login.matches("[a-z]{7}")) {
            throw new ExcecaoDominio("Login deve conter apenas letras minúsculas de a-z");
        }
    }
}
