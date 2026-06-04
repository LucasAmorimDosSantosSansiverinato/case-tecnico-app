package com.desafioTecnico.infra.utils.login;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeradorLoginServiceTest {

    GeradorLoginService gerador;

    @BeforeEach
    void setUp() {
        gerador = new GeradorLoginService();
    }

    @Test
    void deveGerarLoginDe7LetrasMinusculas() {
        String login = gerador.gerar("Maria Silva", List.of());
        assertNotNull(login);
        assertEquals(7, login.length());
        assertTrue(login.matches("[a-z]{7}"));
    }

    @Test
    void deveNormalizarNomeComAcentos() {
        // Acentos devem ser removidos antes de gerar o login
        String login = gerador.gerar("João Silva", List.of());
        assertNotNull(login);
        assertEquals(7, login.length());
        assertTrue(login.matches("[a-z]{7}"));
    }

    @Test
    void deveGerarLoginDiferenteQuandoPrimeiroJaOcupado() {
        // Gera o primeiro candidato sem colisão
        String primeiroLogin = gerador.gerar("Maria Silva", List.of());

        // Agora passa esse login como ocupado — deve gerar outro
        String segundoLogin = gerador.gerar("Maria Silva", List.of(primeiroLogin));

        assertNotNull(segundoLogin);
        assertEquals(7, segundoLogin.length());
        assertTrue(segundoLogin.matches("[a-z]{7}"));
        assertNotEquals(primeiroLogin, segundoLogin);
    }

    @Test
    void deveGerarLoginConsistenteParaMesmoNome() {
        // Sem colisões, o mesmo nome sempre gera o mesmo primeiro candidato
        String login1 = gerador.gerar("Carlos Pereira", List.of());
        String login2 = gerador.gerar("Carlos Pereira", List.of());
        assertEquals(login1, login2);
    }

    @Test
    void deveLancarExcecaoQuandoImpossívelGerarLoginUnico() {
        // Gera todos os candidatos possíveis para um nome muito curto e os marca como ocupados
        // "Al Bo" → "albo" são apenas 4 chars → nenhum candidato de 7 chars é possível
        // Para garantir a falha, usamos o nome e coletamos todos os candidatos possíveis
        GeradorLoginService g = new GeradorLoginService();

        assertThrows(ExcecaoDominio.class,
                () -> g.gerar("Ab Cd", gerarTodosPossiveis()));
    }

    // Gera todos os logins de 7 letras com as letras do nome "ab cd" = "abcd"
    private List<String> gerarTodosPossiveis() {
        // "ab cd" normaliza para "abcd" (4 chars). Qualquer janela de 7 sobre 4 chars é impossível.
        // Porém o algoritmo circular pode gerar "abcdabc" - isso não é letra minúscula pura... na verdade é.
        // Para garantir o esgotamento, retornamos todos os candidatos que o gerador produz para esse nome.
        // Cobrimos os 4 primeiros candidatos e o fallback circular.
        return List.of("abcdabc", "bcdabcd", "cdabcda", "dabcdab");
    }

    @Test
    void deveGerarLoginParaNomeComEspacosExtras() {
        String login = gerador.gerar("  Ana   Lima  ", List.of());
        assertNotNull(login);
        assertEquals(7, login.length());
        assertTrue(login.matches("[a-z]{7}"));
    }

    @Test
    void deveGerarLoginParaNomeComposto() {
        String login = gerador.gerar("Maria das Gracas", List.of());
        assertNotNull(login);
        assertEquals(7, login.length());
        assertTrue(login.matches("[a-z]{7}"));
    }
}
