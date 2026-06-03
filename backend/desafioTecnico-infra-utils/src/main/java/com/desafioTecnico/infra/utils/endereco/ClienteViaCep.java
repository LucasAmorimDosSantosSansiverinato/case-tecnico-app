package com.desafioTecnico.infra.utils.endereco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteViaCep {

    @JsonProperty("cep")
    private String cep;

    @JsonProperty("logradouro")
    private String logradouro;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("localidade")
    private String cidade;

    @JsonProperty("uf")
    private String estado;

    @JsonProperty("erro")
    private Boolean erro;

    public String getCep()        { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getBairro()     { return bairro; }
    public String getCidade()     { return cidade; }
    public String getEstado()     { return estado; }
    public Boolean getErro()      { return erro; }
}
