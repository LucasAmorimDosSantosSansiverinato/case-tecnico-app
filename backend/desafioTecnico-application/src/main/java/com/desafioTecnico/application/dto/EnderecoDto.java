package com.desafioTecnico.application.dto;

public class EnderecoDto {

    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
    private String numero;

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

    public String getCep()                          { return cep; }
    public void setCep(String cep)                  { this.cep = cep; }

    public String getLogradouro()                   { return logradouro; }
    public void setLogradouro(String logradouro)    { this.logradouro = logradouro; }

    public String getBairro()                       { return bairro; }
    public void setBairro(String bairro)            { this.bairro = bairro; }

    public String getCidade()                       { return cidade; }
    public void setCidade(String cidade)            { this.cidade = cidade; }

    public String getEstado()                       { return estado; }
    public void setEstado(String estado)            { this.estado = estado; }

    public String getComplemento()                  { return complemento; }
    public void setComplemento(String complemento)  { this.complemento = complemento; }

    public String getNumero()                       { return numero; }
    public void setNumero(String numero)            { this.numero = numero; }
}
