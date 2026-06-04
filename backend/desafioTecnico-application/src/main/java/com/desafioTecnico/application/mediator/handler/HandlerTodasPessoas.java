package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.EnderecoDto;
import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

import java.util.List;

public class HandlerTodasPessoas {

    private final IRepositorioPessoa repositorioPessoa;

    public HandlerTodasPessoas(IRepositorioPessoa repositorioPessoa) {
        this.repositorioPessoa = repositorioPessoa;
    }

    public List<PessoaDto> handle(QueryTodasPessoas consulta) {
        return repositorioPessoa.listarTodos().stream()
                .map(this::toPessoaDto)
                .toList();
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
