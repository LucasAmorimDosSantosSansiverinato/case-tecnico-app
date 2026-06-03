package com.desafioTecnico.webui.config;

import com.desafioTecnico.application.interface_.IPortaGeradorLogin;
import com.desafioTecnico.application.interface_.IPortaServicoEndereco;
import com.desafioTecnico.application.mediator.handler.HandlerCadastrarPessoa;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPorId;
import com.desafioTecnico.application.mediator.handler.HandlerQueryPorLogin;
import com.desafioTecnico.application.mediator.handler.HandlerTodasPessoas;
import com.desafioTecnico.domain.interface_.IRepositorioPessoa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HandlerCadastrarPessoa HandlerCadastrarPessoa(
            IRepositorioPessoa IRepositorioPessoa,
            IPortaGeradorLogin IPortaGeradorLogin,
            IPortaServicoEndereco IPortaServicoEndereco
    ) {
        return new HandlerCadastrarPessoa(IRepositorioPessoa, IPortaGeradorLogin, IPortaServicoEndereco);
    }

    @Bean
    public HandlerQueryPorId HandlerQueryPorId(IRepositorioPessoa IRepositorioPessoa) {
        return new HandlerQueryPorId(IRepositorioPessoa);
    }

    @Bean
    public HandlerTodasPessoas HandlerTodasPessoas(IRepositorioPessoa IRepositorioPessoa) {
        return new HandlerTodasPessoas(IRepositorioPessoa);
    }

    @Bean
    public HandlerQueryPorLogin HandlerQueryPorLogin(IRepositorioPessoa IRepositorioPessoa) {
        return new HandlerQueryPorLogin(IRepositorioPessoa);
    }
}
