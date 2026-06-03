package com.desafioTecnico.webui.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class RequisicaoCadastro {

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    private String complemento;
    private String numero;

    public RequisicaoCadastro() {}

    public String getNomeCompleto()          { return nomeCompleto; }
    public void setNomeCompleto(String v)    { this.nomeCompleto = v; }

    public String getCpf()                   { return cpf; }
    public void setCpf(String v)             { this.cpf = v; }

    public String getEmail()                 { return email; }
    public void setEmail(String v)           { this.email = v; }

    public LocalDate getDataNascimento()         { return dataNascimento; }
    public void setDataNascimento(LocalDate v)   { this.dataNascimento = v; }

    public String getCep()                   { return cep; }
    public void setCep(String v)             { this.cep = v; }

    public String getComplemento()           { return complemento; }
    public void setComplemento(String v)     { this.complemento = v; }

    public String getNumero()                { return numero; }
    public void setNumero(String v)          { this.numero = v; }
}
