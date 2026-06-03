package com.desafioTecnico.application.query;

public class GetPersonByIdQuery {

    private final String id;

    public GetPersonByIdQuery(String id) {
        this.id = id;
    }

    public String id() { return id; }
}
