package com.desafioTecnico.application.dto;

public class EnderecoDto {

    public String cep;
    public String logradouro;
    public String bairro;
    public String cidade;
    public String estado;
    public String complemento;
    public String numero;

    public EnderecoDto() {}

    public EnderecoDto(String cep, String logradouro, String bairro,
                             String cidade, String estado, String complemento, String numero) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.complemento = complemento;
        this.numero = numero;
    }
}
