package com.desafioTecnico.application.mediator.query;

// Query para buscar uma pessoa pelo seu identificador único
public class QueryPessoa {

    private final String id;

    public QueryPessoa(String id) {
        this.id = id;
    }

    public String getId() { return id; }
}
