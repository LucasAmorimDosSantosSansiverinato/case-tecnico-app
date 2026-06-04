package com.desafioTecnico.infra.utils.validacao;

import com.desafioTecnico.domain.excecao.ExcecaoDominio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorCpfTest {

    ValidadorCpf validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorCpf();
    }

    @Test
    void deveRetornarDigitosQuandoCpfValido() {
        String resultado = validador.validarENormalizar("529.982.247-25");
        assertEquals("52998224725", resultado);
    }

    @Test
    void deveAceitarCpfSemFormatacao() {
        String resultado = validador.validarENormalizar("52998224725");
        assertEquals("52998224725", resultado);
    }

    @Test
    void deveLancarExcecaoQuandoCpfNulo() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> validador.validarENormalizar(null));
        assertTrue(ex.getMessage().contains("obrigatório"));
    }

    @Test
    void deveLancarExcecaoQuandoCpfComMenosDe11Digitos() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> validador.validarENormalizar("1234567"));
        assertTrue(ex.getMessage().contains("11 dígitos"));
    }

    @Test
    void deveLancarExcecaoQuandoCpfComMaisDe11Digitos() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> validador.validarENormalizar("123456789012"));
        assertTrue(ex.getMessage().contains("11 dígitos"));
    }

    // CPF com todos os dígitos iguais é tecnicamente válido pelo algoritmo mas é fraude conhecida
    @Test
    void deveLancarExcecaoQuandoCpfComTodosDigitosIguais() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> validador.validarENormalizar("11111111111"));
        assertTrue(ex.getMessage().contains("inválido"));
    }

    @Test
    void deveLancarExcecaoQuandoPrimeiroDigitoVerificadorErrado() {
        // CPF com dígito verificador modificado: 52998224715 (último correto, penúltimo errado)
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> validador.validarENormalizar("52998224715"));
        assertTrue(ex.getMessage().contains("inválido"));
    }

    @Test
    void deveLancarExcecaoQuandoSegundoDigitoVerificadorErrado() {
        // Penúltimo correto (7), último errado (0 em vez de 5)
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class,
                () -> validador.validarENormalizar("52998224720"));
        assertTrue(ex.getMessage().contains("inválido"));
    }

    @Test
    void deveAceitarOutroCpfValido() {
        // Outro CPF matematicamente válido
        String resultado = validador.validarENormalizar("111.444.777-35");
        assertEquals("11144477735", resultado);
    }
}
