package com.desafioTecnico.application.query;

public class GetPersonByLoginQuery {
    private final String login;

    public GetPersonByLoginQuery(String login) {
        this.login = login;
    }

    public String login() { return login; }
}
