package com.desafioTecnico.ioc;

import com.desafioTecnico.application.interfaces.IPortaGeradorLogin;
import com.desafioTecnico.application.interfaces.IPortaServicoEndereco;
import com.desafioTecnico.application.interfaces.IPortaValidadorCpf;
import com.desafioTecnico.application.mediator.handler.HandlerCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerTodasPessoas;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Camada de inversão de controle: resolve quem implementa cada interface e monta os handlers
@Configuration
public class DependecyInjaction {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Registra o handler de cadastro injetando todas as suas dependências
    @Bean
    public HandlerCadastrarPessoa handlerCadastrarPessoa(
            IRepositorioPessoa repositorioPessoa,
            IPortaGeradorLogin geradorLogin,
            IPortaServicoEndereco servicoEndereco,
            IPortaValidadorCpf validadorCpf
    ) {
        return new HandlerCadastrarPessoa(repositorioPessoa, geradorLogin, servicoEndereco, validadorCpf);
    }

    // Registra o handler de busca individual por id
    @Bean
    public HandlerQueryPessoa handlerQueryPessoa(IRepositorioPessoa repositorioPessoa) {
        return new HandlerQueryPessoa(repositorioPessoa);
    }

    @Bean
    public HandlerTodasPessoas handlerTodasPessoas(IRepositorioPessoa repositorioPessoa) {
        return new HandlerTodasPessoas(repositorioPessoa);
    }
}
