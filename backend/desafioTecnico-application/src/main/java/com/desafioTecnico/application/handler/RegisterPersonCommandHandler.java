package com.desafioTecnico.application.handler;

import com.desafioTecnico.application.command.RegisterPersonCommand;
import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.port.AddressServicePort;
import com.desafioTecnico.application.port.LoginGeneratorPort;
import com.desafioTecnico.domain.entity.Person;
import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.repository.PersonRepository;
import com.desafioTecnico.domain.vo.Address;
import com.desafioTecnico.domain.vo.Document;
import com.desafioTecnico.domain.vo.Login;
import com.desafioTecnico.domain.vo.PersonId;

import java.util.List;

public class RegisterPersonCommandHandler {

    private final PersonRepository personRepository;
    private final LoginGeneratorPort loginGenerator;
    private final AddressServicePort addressService;

    public RegisterPersonCommandHandler(
            PersonRepository personRepository,
            LoginGeneratorPort loginGenerator,
            AddressServicePort addressService
    ) {
        this.personRepository = personRepository;
        this.loginGenerator = loginGenerator;
        this.addressService = addressService;
    }

    public PersonResponse handle(RegisterPersonCommand command) {
        Document document = Document.of(command.document());

        if (personRepository.findByDocument(document.getValue()).isPresent()) {
            throw new DomainException("A person with this document is already registered");
        }
        if (personRepository.findByEmail(command.email()).isPresent()) {
            throw new DomainException("A person with this email is already registered");
        }

        Address baseAddress = addressService.findByCep(command.cep());
        Address address = Address.builder()
                .cep(baseAddress.getCep())
                .street(baseAddress.getStreet())
                .neighborhood(baseAddress.getNeighborhood())
                .city(baseAddress.getCity())
                .state(baseAddress.getState())
                .complement(command.complement())
                .number(command.number())
                .build();

        List<String> existingLogins = personRepository.findAllLogins();
        Login login = loginGenerator.generate(command.fullName(), existingLogins);

        Person person = Person.builder()
                .id(PersonId.generate())
                .fullName(command.fullName())
                .document(document)
                .email(command.email())
                .birthDate(command.birthDate())
                .address(address)
                .login(login)
                .build();

        return PersonResponse.from(personRepository.save(person));
    }
}
