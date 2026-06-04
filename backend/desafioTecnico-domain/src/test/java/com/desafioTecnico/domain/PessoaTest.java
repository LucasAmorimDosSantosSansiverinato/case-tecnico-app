package com.desafioTecnico.domain;

import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    // Cria uma Pessoa com todos os campos válidos para servir de base nos testes
    private Pessoa pessoaValida() {
        return new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Avenida Paulista",
                "Bela Vista", "Sao Paulo", "SP", null, "100", "msilvaa", null
        );
    }

    @Test
    void deveCriarPessoaValida() {
        Pessoa pessoa = pessoaValida();
        assertNotNull(pessoa.getId());
        assertEquals("Maria Silva", pessoa.getNomeCompleto());
        assertEquals("maria@example.com", pessoa.getEmail());
        assertNotNull(pessoa.getCriadoEm());
    }

    @Test
    void deveRejeitarNomeCompletoVazio() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarNomeCompletoNulo() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), null, "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarNomeCompletoSemSobrenome() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarNomeCompletoComNumeros() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria123 Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarNomeCompletoComCaracteresEspeciais() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria@ Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveNormalizarEspacosNoNomeCompleto() {
        Pessoa pessoa = new Pessoa(
                UUID.randomUUID(), "  Maria   Silva  ", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        );
        assertEquals("Maria Silva", pessoa.getNomeCompleto());
    }

    @Test
    void deveRejeitarEmailVazio() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarEmailNulo() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", null,
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "naoemail",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "faltando@dominio",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveConverterEmailParaMinusculo() {
        Pessoa pessoa = new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "Maria@Example.COM",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        );
        assertEquals("maria@example.com", pessoa.getEmail());
    }

    @Test
    void deveRejeitarDataNascimentoNula() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                null, "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarDataNascimentoFutura() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.now().plusDays(1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveAceitarDataNascimentoHoje() {
        assertDoesNotThrow(() -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.now(), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarCpfNulo() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", null, "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveRejeitarLoginNulo() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, null, null
        ));
    }

    @Test
    void deveRejeitarIdNulo() {
        assertThrows(ExcecaoDominio.class, () -> new Pessoa(
                null, "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", null
        ));
    }

    @Test
    void deveSuportarCriadoEmPersonalizado() {
        LocalDateTime tempo = LocalDateTime.of(2024, 6, 1, 12, 0);
        Pessoa pessoa = new Pessoa(
                UUID.randomUUID(), "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista",
                null, "Sao Paulo", "SP", null, null, "msilvaa", tempo
        );
        assertEquals(tempo, pessoa.getCriadoEm());
    }

    @Test
    void pessoasComMesmoIdSaoIguais() {
        UUID id = UUID.randomUUID();
        Pessoa p1 = new Pessoa(id, "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista", null, "Sao Paulo", "SP", null, null, "msilvaa", null);
        Pessoa p2 = new Pessoa(id, "Maria Silva", "52998224725", "maria@example.com",
                LocalDate.of(1990, 1, 1), "01310100", "Av Paulista", null, "Sao Paulo", "SP", null, null, "msilvaa", null);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void pessoasComIdsDiferentesNaoSaoIguais() {
        Pessoa p1 = pessoaValida();
        Pessoa p2 = pessoaValida();
        assertNotEquals(p1, p2);
    }

    @Test
    void naoDeveSerIgualAObjetoNaoPessoa() {
        Pessoa pessoa = pessoaValida();
        assertNotEquals(pessoa, "nao e uma pessoa");
    }
}
