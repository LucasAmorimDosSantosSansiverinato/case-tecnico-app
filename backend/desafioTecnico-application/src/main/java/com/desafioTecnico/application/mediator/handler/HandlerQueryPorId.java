package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorId;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

import java.util.UUID;

public class HandlerQueryPorId {

    private final IRepositorioPessoa IRepositorioPessoa;

    public HandlerQueryPorId(IRepositorioPessoa IRepositorioPessoa) {
        this.IRepositorioPessoa = IRepositorioPessoa;
    }

    public PessoaDto handle(QueryPessoaPorId consulta) {
        return IRepositorioPessoa.buscarPorId(UUID.fromString(consulta.id()))
                .map(PessoaDto::de)
                .orElseThrow(() -> new ExcecaoDominio("Pessoa não encontrada com o id: " + consulta.id()));
    }
}
