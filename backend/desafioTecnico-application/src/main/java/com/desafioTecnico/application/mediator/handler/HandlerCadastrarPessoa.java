package com.desafioTecnico.application.mediator.handler;

import com.desafioTecnico.application.dto.EnderecoDto;
import com.desafioTecnico.application.dto.PessoaDto;
import com.desafioTecnico.application.interfaces.IPortaGeradorLogin;
import com.desafioTecnico.application.interfaces.IPortaServicoEndereco;
import com.desafioTecnico.application.interfaces.IPortaValidadorCpf;
import com.desafioTecnico.application.mediator.command.CommandCadastrarPessoa;
import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HandlerCadastrarPessoa {

    private final IRepositorioPessoa repositorioPessoa;
    private final IPortaGeradorLogin geradorLogin;
    private final IPortaServicoEndereco servicoEndereco;
    private final IPortaValidadorCpf validadorCpf;

    public HandlerCadastrarPessoa(
            IRepositorioPessoa repositorioPessoa,
            IPortaGeradorLogin geradorLogin,
            IPortaServicoEndereco servicoEndereco,
            IPortaValidadorCpf validadorCpf
    ) {
        this.repositorioPessoa = repositorioPessoa;
        this.geradorLogin      = geradorLogin;
        this.servicoEndereco   = servicoEndereco;
        this.validadorCpf      = validadorCpf;
    }

    public PessoaDto handle(CommandCadastrarPessoa comando) {
        // Normaliza e valida o CPF antes de qualquer consulta ao banco
        String cpf = validadorCpf.validarENormalizar(comando.getCpf());

        if (repositorioPessoa.buscarPorCpf(cpf).isPresent())
            throw new ExcecaoDominio("Já existe uma pessoa cadastrada com este CPF");
        if (repositorioPessoa.buscarPorEmail(comando.getEmail()).isPresent())
            throw new ExcecaoDominio("Já existe uma pessoa cadastrada com este e-mail");

        // Busca os dados de endereço automaticamente pelo CEP via ViaCEP
        Map<String, String> endereco = servicoEndereco.buscarPorCep(comando.getCep());

        List<String> loginsExistentes = repositorioPessoa.listarTodosLogins();
        String login = geradorLogin.gerar(comando.getNomeCompleto(), loginsExistentes);

        Pessoa pessoa = new Pessoa(
                UUID.randomUUID(),
                comando.getNomeCompleto(),
                cpf,
                comando.getEmail(),
                comando.getDataNascimento(),
                endereco.get("cep"),
                endereco.getOrDefault("logradouro", ""),
                endereco.get("bairro"),
                endereco.get("cidade"),
                endereco.get("estado"),
                comando.getComplemento(),
                comando.getNumero(),
                login,
                null
        );

        return toPessoaDto(repositorioPessoa.salvar(pessoa));
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
