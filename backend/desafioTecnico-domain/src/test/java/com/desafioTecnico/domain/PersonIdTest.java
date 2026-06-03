package com.desafioTecnico.domain;

import com.desafioTecnico.domain.vo.PersonId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonIdTest {

    @Test
    void shouldGenerateUniqueIds() {
        PersonId id1 = PersonId.generate();
        PersonId id2 = PersonId.generate();
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldCreateFromUUID() {
        UUID uuid = UUID.randomUUID();
        PersonId id = PersonId.of(uuid);
        assertEquals(uuid, id.getValue());
    }

    @Test
    void shouldCreateFromString() {
        String uuid = UUID.randomUUID().toString();
        PersonId id = PersonId.of(uuid);
        assertEquals(uuid, id.toString());
    }

    @Test
    void shouldRejectNullUUID() {
        assertThrows(NullPointerException.class, () -> PersonId.of((UUID) null));
    }

    @Test
    void shouldRejectInvalidStringUUID() {
        assertThrows(IllegalArgumentException.class, () -> PersonId.of("not-a-uuid"));
    }

    @Test
    void idsWithSameValueAreEqual() {
        UUID uuid = UUID.randomUUID();
        PersonId id1 = PersonId.of(uuid);
        PersonId id2 = PersonId.of(uuid);
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotEqualNonPersonIdObject() {
        PersonId id = PersonId.generate();
        assertNotEquals(id, "string");
    }

    @Test
    void toStringReturnsUUIDString() {
        UUID uuid = UUID.randomUUID();
        PersonId id = PersonId.of(uuid);
        assertEquals(uuid.toString(), id.toString());
    }
}
