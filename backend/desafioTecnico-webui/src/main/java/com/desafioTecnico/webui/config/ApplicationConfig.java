package com.desafioTecnico.webui.config;

import com.desafioTecnico.application.handler.GetAllPersonsQueryHandler;
import com.desafioTecnico.application.handler.GetPersonByIdQueryHandler;
import com.desafioTecnico.application.handler.GetPersonByLoginQueryHandler;
import com.desafioTecnico.application.handler.RegisterPersonCommandHandler;
import com.desafioTecnico.application.port.AddressServicePort;
import com.desafioTecnico.application.port.LoginGeneratorPort;
import com.desafioTecnico.domain.repository.PersonRepository;
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
    public RegisterPersonCommandHandler registerPersonCommandHandler(
            PersonRepository personRepository,
            LoginGeneratorPort loginGeneratorPort,
            AddressServicePort addressServicePort
    ) {
        return new RegisterPersonCommandHandler(personRepository, loginGeneratorPort, addressServicePort);
    }

    @Bean
    public GetPersonByIdQueryHandler getPersonByIdQueryHandler(PersonRepository personRepository) {
        return new GetPersonByIdQueryHandler(personRepository);
    }

    @Bean
    public GetAllPersonsQueryHandler getAllPersonsQueryHandler(PersonRepository personRepository) {
        return new GetAllPersonsQueryHandler(personRepository);
    }

    @Bean
    public GetPersonByLoginQueryHandler getPersonByLoginQueryHandler(PersonRepository personRepository) {
        return new GetPersonByLoginQueryHandler(personRepository);
    }
}
