package com.desafioTecnico.infra.data.entidade;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class EntidadePessoaJpa {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String nomeCompleto;

    @Column(name = "document", nullable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "street", nullable = false)
    private String logradouro;

    @Column(name = "neighborhood")
    private String bairro;

    @Column(name = "city", nullable = false)
    private String cidade;

    @Column(name = "state", nullable = false)
    private String estado;

    @Column(name = "complement")
    private String complemento;

    @Column(name = "number")
    private String numero;

    @Column(name = "login", nullable = false, unique = true, length = 7)
    private String login;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    public EntidadePessoaJpa() {}

    public UUID getId()                       { return id; }
    public void setId(UUID id)                { this.id = id; }

    public String getNomeCompleto()           { return nomeCompleto; }
    public void setNomeCompleto(String v)     { this.nomeCompleto = v; }

    public String getCpf()                    { return cpf; }
    public void setCpf(String v)              { this.cpf = v; }

    public String getEmail()                  { return email; }
    public void setEmail(String v)            { this.email = v; }

    public LocalDate getDataNascimento()      { return dataNascimento; }
    public void setDataNascimento(LocalDate v){ this.dataNascimento = v; }

    public String getCep()                    { return cep; }
    public void setCep(String v)              { this.cep = v; }

    public String getLogradouro()             { return logradouro; }
    public void setLogradouro(String v)       { this.logradouro = v; }

    public String getBairro()                 { return bairro; }
    public void setBairro(String v)           { this.bairro = v; }

    public String getCidade()                 { return cidade; }
    public void setCidade(String v)           { this.cidade = v; }

    public String getEstado()                 { return estado; }
    public void setEstado(String v)           { this.estado = v; }

    public String getComplemento()            { return complemento; }
    public void setComplemento(String v)      { this.complemento = v; }

    public String getNumero()                 { return numero; }
    public void setNumero(String v)           { this.numero = v; }

    public String getLogin()                  { return login; }
    public void setLogin(String v)            { this.login = v; }

    public LocalDateTime getCriadoEm()        { return criadoEm; }
    public void setCriadoEm(LocalDateTime v)  { this.criadoEm = v; }
}
