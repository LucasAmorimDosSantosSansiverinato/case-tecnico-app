package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.command.RegisterPersonCommand;
import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.handler.GetAllPersonsQueryHandler;
import com.desafioTecnico.application.handler.GetPersonByIdQueryHandler;
import com.desafioTecnico.application.handler.RegisterPersonCommandHandler;
import com.desafioTecnico.application.query.GetAllPersonsQuery;
import com.desafioTecnico.application.query.GetPersonByIdQuery;
import com.desafioTecnico.webui.dto.RegisterPersonRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final RegisterPersonCommandHandler registerHandler;
    private final GetPersonByIdQueryHandler getByIdHandler;
    private final GetAllPersonsQueryHandler getAllHandler;

    public PersonController(RegisterPersonCommandHandler registerHandler,
                            GetPersonByIdQueryHandler getByIdHandler,
                            GetAllPersonsQueryHandler getAllHandler) {
        this.registerHandler = registerHandler;
        this.getByIdHandler = getByIdHandler;
        this.getAllHandler = getAllHandler;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> register(@Valid @RequestBody RegisterPersonRequest request) {
        RegisterPersonCommand command = new RegisterPersonCommand(
                request.getFullName(),
                request.getDocument(),
                request.getEmail(),
                request.getBirthDate(),
                request.getCep(),
                request.getComplement(),
                request.getNumber()
        );
        PersonResponse response = registerHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getById(@PathVariable String id) {
        PersonResponse response = getByIdHandler.handle(new GetPersonByIdQuery(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAll() {
        List<PersonResponse> response = getAllHandler.handle(new GetAllPersonsQuery());
        return ResponseEntity.ok(response);
    }
}
