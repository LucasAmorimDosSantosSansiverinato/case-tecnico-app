package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.query.GetPersonByIdQuery;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.repository.PersonRepository;
import com.desafioTecnico.domain.vo.PersonId;

public class GetPersonByIdQueryHandler {

    private final PersonRepository personRepository;

    public GetPersonByIdQueryHandler(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse handle(GetPersonByIdQuery query) {
        return personRepository.findById(PersonId.of(query.id()))
                .map(PersonResponse::from)
                .orElseThrow(() -> new DomainException("Person not found with id: " + query.id()));
    }
}
