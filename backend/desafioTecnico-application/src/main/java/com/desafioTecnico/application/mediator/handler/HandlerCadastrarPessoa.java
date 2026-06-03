package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.interface_.IPortaGeradorLogin;
import com.desafioTecnico.application.interface_.IPortaServicoEndereco;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HandlerCadastrarPessoa {

    private final IRepositorioPessoa IRepositorioPessoa;
    private final IPortaGeradorLogin geradorLogin;
    private final IPortaServicoEndereco servicoEndereco;

    public HandlerCadastrarPessoa(
            IRepositorioPessoa IRepositorioPessoa,
            IPortaGeradorLogin geradorLogin,
            IPortaServicoEndereco servicoEndereco
    ) {
        this.IRepositorioPessoa = IRepositorioPessoa;
        this.geradorLogin = geradorLogin;
        this.servicoEndereco = servicoEndereco;
    }

    public PessoaDto handle(CommandCadastrarPessoa comando) {
        String cpfDigitos = comando.cpf().replaceAll("[^0-9]", "");

        if (IRepositorioPessoa.buscarPorCpf(cpfDigitos).isPresent()) {
            throw new ExcecaoDominio("Já existe uma pessoa cadastrada com este CPF");
        }
        if (IRepositorioPessoa.buscarPorEmail(comando.email()).isPresent()) {
            throw new ExcecaoDominio("Já existe uma pessoa cadastrada com este e-mail");
        }

        Map<String, String> dadosEndereco = servicoEndereco.buscarPorCep(comando.cep());

        List<String> loginsExistentes = IRepositorioPessoa.listarTodosLogins();
        String login = geradorLogin.gerar(comando.nomeCompleto(), loginsExistentes);

        Pessoa pessoa = Pessoa.builder()
                .id(UUID.randomUUID())
                .nomeCompleto(comando.nomeCompleto())
                .cpf(cpfDigitos)
                .email(comando.email())
                .dataNascimento(comando.dataNascimento())
                .cep(dadosEndereco.get("cep"))
                .logradouro(dadosEndereco.getOrDefault("logradouro", ""))
                .bairro(dadosEndereco.get("bairro"))
                .cidade(dadosEndereco.get("cidade"))
                .estado(dadosEndereco.get("estado"))
                .complemento(comando.complemento())
                .numero(comando.numero())
                .login(login)
                .build();

        return PessoaDto.de(IRepositorioPessoa.salvar(pessoa));
    }
}
