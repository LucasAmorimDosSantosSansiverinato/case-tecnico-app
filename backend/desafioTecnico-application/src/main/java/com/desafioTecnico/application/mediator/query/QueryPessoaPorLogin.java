package com.desafioTecnico.application.mediator.query;

public class QueryPessoaPorLogin {

    private final String login;

    public QueryPessoaPorLogin(String login) {
        this.login = login;
    }

    public String login() { return login; }
}
