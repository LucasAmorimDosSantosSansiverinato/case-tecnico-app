package com.desafioTecnico.domain.excecao;

//tratamento de exceção para gartir taratamento do stack tracer e nao expor informações sensiveis 
public class ExcecaoDominio extends RuntimeException {

    public ExcecaoDominio(String mensagem) {
        super(mensagem);
    }

    public ExcecaoDominio(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
