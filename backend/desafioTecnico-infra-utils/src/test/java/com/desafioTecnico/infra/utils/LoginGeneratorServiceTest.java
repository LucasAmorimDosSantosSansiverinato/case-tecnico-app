package com.desafioTecnico.infra.utils;

import com.desafioTecnico.infra.utils.login.LoginGeneratorService;
import com.desafioTecnico.domain.vo.Login;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoginGeneratorServiceTest {

    private final LoginGeneratorService service = new LoginGeneratorService();

    @Test
    void shouldGenerateLoginForMariaSilvaSouza() {
        Login login = service.generate("Maria Silva Souza", List.of());
        assertEquals(7, login.getValue().length());
        assertTrue(login.getValue().matches("[a-z]{7}"));
    }

    @Test
    void shouldGenerateUniqueLoginWhenFirstCandidateTaken() {
        Login first = service.generate("Maria Silva Souza", List.of());
        Login second = service.generate("Maria Silva Souza", List.of(first.getValue()));
        assertNotEquals(first.getValue(), second.getValue());
        assertEquals(7, second.getValue().length());
    }

    @Test
    void shouldGenerateLoginForJoaoPedroLima() {
        Login login = service.generate("Joao Pedro Lima", List.of());
        assertEquals(7, login.getValue().length());
        assertTrue(login.getValue().matches("[a-z]{7}"));
    }

    @Test
    void shouldHandleAccentedNames() {
        Login login = service.generate("João Pedro Lima", List.of());
        assertEquals(7, login.getValue().length());
        assertTrue(login.getValue().matches("[a-z]{7}"));
    }

    @Test
    void shouldGenerateManyUniqueLogins() {
        List<String> taken = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Login login = service.generate("Ana Paula Ferreira", taken);
            assertFalse(taken.contains(login.getValue()), "Login collision at iteration " + i);
            taken.add(login.getValue());
        }
    }
}
