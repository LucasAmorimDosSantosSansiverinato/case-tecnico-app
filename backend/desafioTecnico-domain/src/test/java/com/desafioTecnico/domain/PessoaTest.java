package com.desafioTecnico.domain;

import com.desafioTecnico.domain.entidade.Pessoa;
import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    private Pessoa.Builder builderValido() {
        return Pessoa.builder()
                .id(UUID.randomUUID())
                .nomeCompleto("Maria Silva")
                .cpf("52998224725")
                .email("maria@example.com")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .cep("01310100")
                .logradouro("Avenida Paulista")
                .cidade("Sao Paulo")
                .estado("SP")
                .login("msilvaa");
    }

    @Test
    void deveCriarPessoaValida() {
        Pessoa pessoa = builderValido().build();
        assertNotNull(pessoa.getId());
        assertEquals("Maria Silva", pessoa.getNomeCompleto());
        assertEquals("maria@example.com", pessoa.getEmail());
        assertNotNull(pessoa.getCriadoEm());
    }

    @Test
    void deveRejeitarNomeCompletoVazio() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().nomeCompleto("").build());
    }

    @Test
    void deveRejeitarNomeCompletoNulo() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().nomeCompleto(null).build());
    }

    @Test
    void deveRejeitarNomeCompletoSemSobrenome() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().nomeCompleto("Maria").build());
    }

    @Test
    void deveRejeitarNomeCompletoComNumeros() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().nomeCompleto("Maria123 Silva").build());
    }

    @Test
    void deveRejeitarNomeCompletoComCaracteresEspeciais() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().nomeCompleto("Maria@ Silva").build());
    }

    @Test
    void deveNormalizarEspacosNoNomeCompleto() {
        Pessoa pessoa = builderValido().nomeCompleto("  Maria   Silva  ").build();
        assertEquals("Maria Silva", pessoa.getNomeCompleto());
    }

    @Test
    void deveRejeitarEmailVazio() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().email("").build());
    }

    @Test
    void deveRejeitarEmailNulo() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().email(null).build());
    }

    @Test
    void deveRejeitarEmailInvalido() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().email("naoemail").build());
        assertThrows(ExcecaoDominio.class, () -> builderValido().email("faltando@dominio").build());
    }

    @Test
    void deveConverterEmailParaMinusculo() {
        Pessoa pessoa = builderValido().email("Maria@Example.COM").build();
        assertEquals("maria@example.com", pessoa.getEmail());
    }

    @Test
    void deveRejeitarDataNascimentoNula() {
        assertThrows(ExcecaoDominio.class, () -> builderValido().dataNascimento(null).build());
    }

    @Test
    void deveRejeitarDataNascimentoFutura() {
        assertThrows(ExcecaoDominio.class, () ->
                builderValido().dataNascimento(LocalDate.now().plusDays(1)).build());
    }

    @Test
    void deveAceitarDataNascimentoHoje() {
        assertDoesNotThrow(() -> builderValido().dataNascimento(LocalDate.now()).build());
    }

    @Test
    void deveRejeitarCpfNulo() {
        assertThrows(RuntimeException.class, () -> builderValido().cpf(null).build());
    }

    @Test
    void deveRejeitarLoginNulo() {
        assertThrows(RuntimeException.class, () -> builderValido().login(null).build());
    }

    @Test
    void deveRejeitarIdNulo() {
        assertThrows(RuntimeException.class, () ->
                Pessoa.builder()
                        .nomeCompleto("Maria Silva")
                        .cpf("52998224725")
                        .email("maria@example.com")
                        .dataNascimento(LocalDate.of(1990, 1, 1))
                        .cep("01310100")
                        .logradouro("Av Paulista")
                        .cidade("SP")
                        .estado("SP")
                        .login("msilvaa")
                        .build());
    }

    @Test
    void deveSuportarCriadoEmPersonalizado() {
        LocalDateTime tempo = LocalDateTime.of(2024, 6, 1, 12, 0);
        Pessoa pessoa = builderValido().criadoEm(tempo).build();
        assertEquals(tempo, pessoa.getCriadoEm());
    }

    @Test
    void pessoasComMesmoIdSaoIguais() {
        UUID id = UUID.randomUUID();
        Pessoa p1 = builderValido().id(id).build();
        Pessoa p2 = builderValido().id(id).build();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void pessoasComIdsDiferentesNaoSaoIguais() {
        Pessoa p1 = builderValido().id(UUID.randomUUID()).build();
        Pessoa p2 = builderValido().id(UUID.randomUUID()).build();
        assertNotEquals(p1, p2);
    }

    @Test
    void naoDeveSerIgualAObjetoNaoPessoa() {
        Pessoa pessoa = builderValido().build();
        assertNotEquals(pessoa, "nao e uma pessoa");
    }
}
