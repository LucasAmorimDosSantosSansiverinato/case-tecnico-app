package com.desafioTecnico.application.mediator.command;

import java.time.LocalDate;

public class CommandCadastrarPessoa {

    private final String nomeCompleto;
    private final String cpf;
    private final String email;
    private final LocalDate dataNascimento;
    private final String cep;
    private final String complemento;
    private final String numero;

    public CommandCadastrarPessoa(String nomeCompleto, String cpf, String email,
                                   LocalDate dataNascimento, String cep,
                                   String complemento, String numero) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.cep = cep;
        this.complemento = complemento;
        this.numero = numero;
    }

    public String nomeCompleto()        { return nomeCompleto; }
    public String cpf()                 { return cpf; }
    public String email()               { return email; }
    public LocalDate dataNascimento()   { return dataNascimento; }
    public String cep()                 { return cep; }
    public String complemento()         { return complemento; }
    public String numero()              { return numero; }
}
