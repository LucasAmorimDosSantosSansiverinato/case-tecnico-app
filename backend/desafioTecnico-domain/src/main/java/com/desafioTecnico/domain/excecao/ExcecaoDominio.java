package com.desafioTecnico.domain.excecao;

public class ExcecaoDominio extends RuntimeException {

    public ExcecaoDominio(String mensagem) {
        super(mensagem);
    }

    public ExcecaoDominio(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
