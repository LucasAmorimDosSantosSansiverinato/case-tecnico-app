package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.query.GetAllPersonsQuery;
import com.desafioTecnico.domain.repository.PersonRepository;

import java.util.List;

public class GetAllPersonsQueryHandler {

    private final PersonRepository personRepository;

    public GetAllPersonsQueryHandler(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonResponse> handle(GetAllPersonsQuery query) {
        return personRepository.findAll()
                .stream()
                .map(PersonResponse::from)
                .toList();
    }
}
