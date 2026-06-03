package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.query.GetPersonByLoginQuery;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.repository.PersonRepository;

public class GetPersonByLoginQueryHandler {

    private final PersonRepository personRepository;

    public GetPersonByLoginQueryHandler(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse handle(GetPersonByLoginQuery query) {
        return personRepository.findByLogin(query.login())
                .map(PersonResponse::from)
                .orElseThrow(() -> new DomainException("Login não encontrado: " + query.login()));
    }
}
