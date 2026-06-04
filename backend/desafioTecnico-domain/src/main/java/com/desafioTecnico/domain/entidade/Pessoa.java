package com.desafioTecnico.domain.entidade;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class Pessoa {

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

    
    protected Pessoa() {}

    public Pessoa(UUID id, String nomeCompleto, String cpf, String email,
                  LocalDate dataNascimento, String cep, String logradouro,
                  String bairro, String cidade, String estado,
                  String complemento, String numero, String login,
                  LocalDateTime criadoEm) {

        if (id == null) throw new ExcecaoDominio("Id é obrigatório");

        this.id = id;
        this.criadoEm = criadoEm != null ? criadoEm : LocalDateTime.now();

        setNomeCompleto(nomeCompleto);
        setCpf(cpf);
        setEmail(email);
        setDataNascimento(dataNascimento);
        setCep(cep);
        setLogradouro(logradouro);
        setBairro(bairro);
        setCidade(cidade);
        setEstado(estado);
        setComplemento(complemento);
        setNumero(numero);
        setLogin(login);
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

    public void setNomeCompleto(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isBlank())
            throw new ExcecaoDominio("Nome completo é obrigatório");
        String normalizado = nomeCompleto.trim().replaceAll("\\s+", " ");
        if (!normalizado.matches("[a-zA-Z ]+"))
            throw new ExcecaoDominio("Nome completo deve conter apenas letras e espaços (sem acentos ou caracteres especiais)");
        if (normalizado.split(" ").length < 2)
            throw new ExcecaoDominio("Nome completo deve conter pelo menos nome e sobrenome");
        this.nomeCompleto = normalizado;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) throw new ExcecaoDominio("CPF é obrigatório");
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new ExcecaoDominio("E-mail é obrigatório");
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new ExcecaoDominio("Formato de e-mail inválido");
        this.email = email.toLowerCase().trim();
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null) throw new ExcecaoDominio("Data de nascimento é obrigatória");
        if (dataNascimento.isAfter(LocalDate.now()))
            throw new ExcecaoDominio("Data de nascimento não pode ser no futuro");
        this.dataNascimento = dataNascimento;
    }

    public void setCep(String cep) {
        if (cep == null || cep.isBlank()) throw new ExcecaoDominio("CEP é obrigatório");
        this.cep = cep;
    }

    public void setLogradouro(String logradouro) { this.logradouro = logradouro != null ? logradouro : ""; }
    public void setBairro(String bairro)         { this.bairro = bairro; }
    public void setCidade(String cidade)         { this.cidade = cidade; }
    public void setEstado(String estado)         { this.estado = estado; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public void setNumero(String numero)         { this.numero = numero; }

    public void setLogin(String login) {
        if (login == null || login.isBlank()) throw new ExcecaoDominio("Login é obrigatório");
        if (login.length() != 7 || !login.matches("[a-z]{7}"))
            throw new ExcecaoDominio("Login deve ter exatamente 7 letras minúsculas");
        this.login = login;
    }

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
}
