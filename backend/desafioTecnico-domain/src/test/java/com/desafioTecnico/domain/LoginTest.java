package com.desafioTecnico.domain;

import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Login;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void shouldCreateValidLogin() {
        Login login = Login.of("mariasi");
        assertEquals("mariasi", login.getValue());
    }

    @Test
    void shouldRejectLoginWithNumbers() {
        assertThrows(DomainException.class, () -> Login.of("maria12"));
    }

    @Test
    void shouldRejectLoginWithWrongLength() {
        assertThrows(DomainException.class, () -> Login.of("maria"));
        assertThrows(DomainException.class, () -> Login.of("mariasilva"));
    }

    @Test
    void shouldRejectLoginWithUppercase() {
        assertThrows(DomainException.class, () -> Login.of("MariaS1"));
    }

    @Test
    void shouldRejectBlankLogin() {
        assertThrows(DomainException.class, () -> Login.of(""));
        assertThrows(DomainException.class, () -> Login.of(null));
    }
}
