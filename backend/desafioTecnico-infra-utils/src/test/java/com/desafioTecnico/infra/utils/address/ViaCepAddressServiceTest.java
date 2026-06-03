package com.desafioTecnico.infra.utils.address;

import com.desafioTecnico.domain.exception.DomainException;
import com.desafioTecnico.domain.vo.Address;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Uses manual RestTemplate subclass stubs to avoid Mockito concrete-class
 * mocking limitations on Java 21 without the byte-buddy agent.
 */
class ViaCepAddressServiceTest {

    // ── helpers ────────────────────────────────────────────────────────────

    private static ViaCepClient buildClient(String cep, String street,
                                             String neighborhood, String city,
                                             String state, Boolean error) {
        try {
            ViaCepClient client = new ViaCepClient();
            set(client, "cep", cep);
            set(client, "street", street);
            set(client, "neighborhood", neighborhood);
            set(client, "city", city);
            set(client, "state", state);
            set(client, "error", error);
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void set(Object obj, String field, Object val) throws Exception {
        var f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, val);
    }

    /** Stub that returns a pre-configured ViaCepClient. */
    private static RestTemplate returning(ViaCepClient response) {
        return new RestTemplate() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T getForObject(String url, Class<T> responseType, Object... uriVars) {
                return (T) response;
            }
        };
    }

    /** Stub that throws a RestClientException. */
    private static RestTemplate throwing(RuntimeException ex) {
        return new RestTemplate() {
            @Override
            public <T> T getForObject(String url, Class<T> responseType, Object... uriVars) {
                throw ex;
            }
        };
    }

    // ── tests ─────────────────────────────────────────────────────────────

    @Test
    void shouldReturnAddressForValidCep() {
        ViaCepClient response = buildClient("01310-100", "Avenida Paulista",
                "Bela Vista", "Sao Paulo", "SP", null);
        ViaCepAddressService service = new ViaCepAddressService(returning(response));

        Address address = service.findByCep("01310100");

        assertEquals("01310100", address.getCep());
        assertEquals("Avenida Paulista", address.getStreet());
        assertEquals("Sao Paulo", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("Bela Vista", address.getNeighborhood());
    }

    @Test
    void shouldStripFormattingFromCepBeforeCall() {
        ViaCepClient response = buildClient("01310-100", "Av Paulista",
                "Bela Vista", "Sao Paulo", "SP", null);
        ViaCepAddressService service = new ViaCepAddressService(returning(response));

        Address address = service.findByCep("01310-100");

        assertEquals("01310100", address.getCep());
    }

    @Test
    void shouldThrowWhenCepHasWrongLength() {
        ViaCepAddressService service = new ViaCepAddressService(new RestTemplate());

        assertThrows(DomainException.class, () -> service.findByCep("0131010"));
        assertThrows(DomainException.class, () -> service.findByCep("013101000"));
    }

    @Test
    void shouldThrowWhenResponseIsNull() {
        ViaCepAddressService service = new ViaCepAddressService(returning(null));

        DomainException ex = assertThrows(DomainException.class,
                () -> service.findByCep("01310100"));
        assertTrue(ex.getMessage().contains("CEP") || ex.getMessage().contains("not found"));
    }

    @Test
    void shouldThrowWhenResponseHasErrorTrue() {
        ViaCepClient errorResponse = buildClient(null, null, null, null, null, Boolean.TRUE);
        ViaCepAddressService service = new ViaCepAddressService(returning(errorResponse));

        DomainException ex = assertThrows(DomainException.class,
                () -> service.findByCep("99999999"));
        assertTrue(ex.getMessage().contains("CEP") || ex.getMessage().contains("not found"));
    }

    @Test
    void shouldThrowWhenRestTemplateThrows() {
        ViaCepAddressService service = new ViaCepAddressService(
                throwing(new RestClientException("connection refused")));

        DomainException ex = assertThrows(DomainException.class,
                () -> service.findByCep("01310100"));
        assertTrue(ex.getMessage().contains("Failed") || ex.getMessage().contains("ViaCEP"));
    }

    @Test
    void shouldHandleNullStreetInResponse() {
        ViaCepClient response = buildClient("01310-100", null,
                "Bela Vista", "Sao Paulo", "SP", null);
        ViaCepAddressService service = new ViaCepAddressService(returning(response));

        Address address = service.findByCep("01310100");

        assertEquals("", address.getStreet());
    }
}
