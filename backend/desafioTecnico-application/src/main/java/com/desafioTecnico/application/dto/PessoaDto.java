package com.desafioTecnico.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PessoaDto {

    private String id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private EnderecoDto endereco;
    private String login;
    private LocalDateTime criadoEm;

    public PessoaDto() {}

    public PessoaDto(String id, String nomeCompleto, String cpf, String email,
                     LocalDate dataNascimento, EnderecoDto endereco,
                     String login, LocalDateTime criadoEm) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.login = login;
        this.criadoEm = criadoEm;
    }

    public String getId()                              { return id; }
    public void setId(String id)                       { this.id = id; }

    public String getNomeCompleto()                    { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto)   { this.nomeCompleto = nomeCompleto; }

    public String getCpf()                             { return cpf; }
    public void setCpf(String cpf)                     { this.cpf = cpf; }

    public String getEmail()                           { return email; }
    public void setEmail(String email)                 { this.email = email; }

    public LocalDate getDataNascimento()               { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public EnderecoDto getEndereco()                   { return endereco; }
    public void setEndereco(EnderecoDto endereco)      { this.endereco = endereco; }

    public String getLogin()                           { return login; }
    public void setLogin(String login)                 { this.login = login; }

    public LocalDateTime getCriadoEm()                 { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm)    { this.criadoEm = criadoEm; }
}
