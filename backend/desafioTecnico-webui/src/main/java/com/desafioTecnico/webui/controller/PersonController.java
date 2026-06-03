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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

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
        log.info("[BACKEND] POST /api/v1/persons - cadastrando: {}", request.getFullName());
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
        log.info("[BACKEND] Pessoa cadastrada com sucesso - login gerado: {}", response.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getById(@PathVariable String id) {
        log.info("[BACKEND] GET /api/v1/persons/{}", id);
        PersonResponse response = getByIdHandler.handle(new GetPersonByIdQuery(id));
        log.info("[BACKEND] Pessoa encontrada: {}", response.getFullName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAll() {
        log.info("[BACKEND] GET /api/v1/persons - buscando todas as pessoas");
        List<PersonResponse> response = getAllHandler.handle(new GetAllPersonsQuery());
        log.info("[BACKEND] Retornando {} pessoas do banco", response.size());
        return ResponseEntity.ok(response);
    }
}
