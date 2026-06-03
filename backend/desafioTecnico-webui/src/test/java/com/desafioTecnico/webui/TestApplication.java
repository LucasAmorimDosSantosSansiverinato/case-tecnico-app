package com.desafioTecnico.webui;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot application for slice tests.
 * Does not enable JPA repositories or entity scanning,
 * so @WebMvcTest slices load cleanly without a DataSource.
 */
@SpringBootApplication
public class TestApplication {
}
