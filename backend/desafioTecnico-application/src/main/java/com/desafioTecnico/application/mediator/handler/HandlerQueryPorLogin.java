package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.mediator.query.QueryPessoaPorLogin;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

public class HandlerQueryPorLogin {

    private final IRepositorioPessoa IRepositorioPessoa;

    public HandlerQueryPorLogin(IRepositorioPessoa IRepositorioPessoa) {
        this.IRepositorioPessoa = IRepositorioPessoa;
    }

    public PessoaDto handle(QueryPessoaPorLogin consulta) {
        return IRepositorioPessoa.buscarPorLogin(consulta.login())
                .map(PessoaDto::de)
                .orElseThrow(() -> new ExcecaoDominio("Login não encontrado: " + consulta.login()));
    }
}
