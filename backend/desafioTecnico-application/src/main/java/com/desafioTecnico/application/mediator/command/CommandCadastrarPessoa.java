package com.desafioTecnico.application.mediator.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

// Carrega os dados de entrada para o cadastro; endereço é preenchido automaticamente via ViaCEP
public class CommandCadastrarPessoa {

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

    public CommandCadastrarPessoa() {}

    public String getNomeCompleto()                        { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto)       { this.nomeCompleto = nomeCompleto; }

    public String getCpf()                                 { return cpf; }
    public void setCpf(String cpf)                         { this.cpf = cpf; }

    public String getEmail()                               { return email; }
    public void setEmail(String email)                     { this.email = email; }

    public LocalDate getDataNascimento()                    { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCep()                                 { return cep; }
    public void setCep(String cep)                         { this.cep = cep; }

    public String getComplemento()                         { return complemento; }
    public void setComplemento(String complemento)         { this.complemento = complemento; }

    public String getNumero()                              { return numero; }
    public void setNumero(String numero)                   { this.numero = numero; }
}
