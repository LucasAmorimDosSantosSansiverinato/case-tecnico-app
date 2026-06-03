package com.desafioTecnico.webui.controller;

import com.desafioTecnico.application.dto.AddressResponse;
import com.desafioTecnico.application.dto.PersonResponse;
import com.desafioTecnico.application.handler.GetAllPersonsQueryHandler;
import com.desafioTecnico.application.handler.GetPersonByIdQueryHandler;
import com.desafioTecnico.application.handler.RegisterPersonCommandHandler;
import com.desafioTecnico.domain.exception.DomainException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class)
@ContextConfiguration(classes = com.desafioTecnico.webui.TestApplication.class)
@Import(com.desafioTecnico.webui.handler.GlobalExceptionHandler.class)
class PersonControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean RegisterPersonCommandHandler registerHandler;
    @MockBean GetPersonByIdQueryHandler getByIdHandler;
    @MockBean GetAllPersonsQueryHandler getAllHandler;

    private PersonResponse buildPersonResponse(String id, String name) {
        AddressResponse address = new AddressResponse(
                "01310100", "Av Paulista", "Bela Vista", "Sao Paulo", "SP", null, "100"
        );
        return new PersonResponse(
                id, name, "529.982.247-25", "maria@example.com",
                LocalDate.of(1990, 1, 1), address, "msilva1", LocalDateTime.now()
        );
    }

    @Test
    void shouldReturn201WhenPersonRegistered() throws Exception {
        String id = UUID.randomUUID().toString();
        when(registerHandler.handle(any())).thenReturn(buildPersonResponse(id, "Maria Silva"));

        String body = """
                {
                    "fullName": "Maria Silva",
                    "document": "529.982.247-25",
                    "email": "maria@example.com",
                    "birthDate": "1990-01-01",
                    "cep": "01310100",
                    "number": "100"
                }
                """;

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Maria Silva"))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void shouldReturn422WhenDomainExceptionOnRegister() throws Exception {
        when(registerHandler.handle(any())).thenThrow(new DomainException("A person with this document is already registered"));

        String body = """
                {
                    "fullName": "Maria Silva",
                    "document": "529.982.247-25",
                    "email": "maria@example.com",
                    "birthDate": "1990-01-01",
                    "cep": "01310100"
                }
                """;

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn400WhenRequestBodyInvalid() throws Exception {
        String body = """
                {
                    "fullName": "",
                    "document": "",
                    "email": "not-an-email",
                    "cep": ""
                }
                """;

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenGetById() throws Exception {
        String id = UUID.randomUUID().toString();
        when(getByIdHandler.handle(any())).thenReturn(buildPersonResponse(id, "Maria Silva"));

        mockMvc.perform(get("/api/v1/persons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void shouldReturn422WhenPersonNotFound() throws Exception {
        when(getByIdHandler.handle(any())).thenThrow(new DomainException("Person not found"));

        mockMvc.perform(get("/api/v1/persons/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn200WithListWhenGetAll() throws Exception {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        when(getAllHandler.handle(any())).thenReturn(List.of(
                buildPersonResponse(id1, "Maria Silva"),
                buildPersonResponse(id2, "Joao Lima")
        ));

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturn200WithEmptyListWhenNoPersons() throws Exception {
        when(getAllHandler.handle(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturn500OnUnexpectedException() throws Exception {
        when(getAllHandler.handle(any())).thenThrow(new RuntimeException("unexpected"));

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isInternalServerError());
    }
}
