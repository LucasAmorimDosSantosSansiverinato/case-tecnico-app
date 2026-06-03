package com.desafioTecnico.domain.entidade;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Pessoa {

    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
    private String numero;
    private String login;
    private LocalDateTime criadoEm;

    private Pessoa(Builder builder) {
        this.id = builder.id;
        this.nomeCompleto = builder.nomeCompleto;
        this.cpf = builder.cpf;
        this.email = builder.email;
        this.dataNascimento = builder.dataNascimento;
        this.cep = builder.cep;
        this.logradouro = builder.logradouro;
        this.bairro = builder.bairro;
        this.cidade = builder.cidade;
        this.estado = builder.estado;
        this.complemento = builder.complemento;
        this.numero = builder.numero;
        this.login = builder.login;
        this.criadoEm = builder.criadoEm;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId()                  { return id; }
    public String getNomeCompleto()      { return nomeCompleto; }
    public String getCpf()               { return cpf; }
    public String getEmail()             { return email; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCep()               { return cep; }
    public String getLogradouro()        { return logradouro; }
    public String getBairro()            { return bairro; }
    public String getCidade()            { return cidade; }
    public String getEstado()            { return estado; }
    public String getComplemento()       { return complemento; }
    public String getNumero()            { return numero; }
    public String getLogin()             { return login; }
    public LocalDateTime getCriadoEm()   { return criadoEm; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private UUID id;
        private String nomeCompleto;
        private String cpf;
        private String email;
        private LocalDate dataNascimento;
        private String cep;
        private String logradouro;
        private String bairro;
        private String cidade;
        private String estado;
        private String complemento;
        private String numero;
        private String login;
        private LocalDateTime criadoEm = LocalDateTime.now();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder nomeCompleto(String nomeCompleto) {
            this.nomeCompleto = validarNomeCompleto(nomeCompleto);
            return this;
        }

        public Builder cpf(String cpf) {
            this.cpf = Objects.requireNonNull(cpf, "CPF é obrigatório");
            return this;
        }

        public Builder email(String email) {
            this.email = validarEmail(email);
            return this;
        }

        public Builder dataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = validarDataNascimento(dataNascimento);
            return this;
        }

        public Builder cep(String cep) {
            this.cep = cep;
            return this;
        }

        public Builder logradouro(String logradouro) {
            this.logradouro = logradouro;
            return this;
        }

        public Builder bairro(String bairro) {
            this.bairro = bairro;
            return this;
        }

        public Builder cidade(String cidade) {
            this.cidade = cidade;
            return this;
        }

        public Builder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public Builder complemento(String complemento) {
            this.complemento = complemento;
            return this;
        }

        public Builder numero(String numero) {
            this.numero = numero;
            return this;
        }

        public Builder login(String login) {
            this.login = Objects.requireNonNull(login, "Login é obrigatório");
            return this;
        }

        public Builder criadoEm(LocalDateTime criadoEm) {
            this.criadoEm = criadoEm;
            return this;
        }

        public Pessoa build() {
            Objects.requireNonNull(id, "Id é obrigatório");
            Objects.requireNonNull(login, "Login é obrigatório");
            return new Pessoa(this);
        }

        private String validarNomeCompleto(String nome) {
            if (nome == null || nome.isBlank()) {
                throw new ExcecaoDominio("Nome completo é obrigatório");
            }
            String normalizado = nome.trim().replaceAll("\\s+", " ");
            if (!normalizado.matches("[a-zA-Z ]+")) {
                throw new ExcecaoDominio("Nome completo deve conter apenas letras e espaços (sem acentos ou caracteres especiais)");
            }
            if (normalizado.split(" ").length < 2) {
                throw new ExcecaoDominio("Nome completo deve conter pelo menos nome e sobrenome");
            }
            return normalizado;
        }

        private String validarEmail(String email) {
            if (email == null || email.isBlank()) {
                throw new ExcecaoDominio("E-mail é obrigatório");
            }
            if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                throw new ExcecaoDominio("Formato de e-mail inválido");
            }
            return email.toLowerCase().trim();
        }

        private LocalDate validarDataNascimento(LocalDate data) {
            if (data == null) {
                throw new ExcecaoDominio("Data de nascimento é obrigatória");
            }
            if (data.isAfter(LocalDate.now())) {
                throw new ExcecaoDominio("Data de nascimento não pode ser no futuro");
            }
            return data;
        }
    }
}
