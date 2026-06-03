package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.query.QueryTodasPessoas;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

import java.util.List;

public class HandlerTodasPessoas {

    private final IRepositorioPessoa IRepositorioPessoa;

    public HandlerTodasPessoas(IRepositorioPessoa IRepositorioPessoa) {
        this.IRepositorioPessoa = IRepositorioPessoa;
    }

    public List<PessoaDto> handle(QueryTodasPessoas consulta) {
        return IRepositorioPessoa.listarTodos()
                .stream()
                .map(PessoaDto::de)
                .toList();
    }
}
