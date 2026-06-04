package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.EnderecoDto;
import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.query.QueryPessoa;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

import java.util.UUID;

public class HandlerQueryPessoa {

    private final IRepositorioPessoa repositorioPessoa;

    public HandlerQueryPessoa(IRepositorioPessoa repositorioPessoa) {
        this.repositorioPessoa = repositorioPessoa;
    }

    public PessoaDto handle(QueryPessoa consulta) {
        Pessoa pessoa = repositorioPessoa.buscarPorId(UUID.fromString(consulta.getId()))
                .orElseThrow(() -> new ExcecaoDominio("Pessoa não encontrada com o id: " + consulta.getId()));
        return toPessoaDto(pessoa);
    }

    private PessoaDto toPessoaDto(Pessoa pessoa) {
        EnderecoDto enderecoDto = new EnderecoDto(
                pessoa.getCep(), pessoa.getLogradouro(), pessoa.getBairro(),
                pessoa.getCidade(), pessoa.getEstado(), pessoa.getComplemento(), pessoa.getNumero()
        );
        return new PessoaDto(
                pessoa.getId().toString(), pessoa.getNomeCompleto(), pessoa.getCpf(),
                pessoa.getEmail(), pessoa.getDataNascimento(), enderecoDto,
                pessoa.getLogin(), pessoa.getCriadoEm()
        );
    }
}
