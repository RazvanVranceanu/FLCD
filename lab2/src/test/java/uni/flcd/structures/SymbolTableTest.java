package uni.flcd.structures;

import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uni.flcd.exceptions.ExistentElementException;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest {
    private SymbolTable victim;

    @BeforeEach
    void setup() {
        victim = new SymbolTable();
    }

    @Test
    @DisplayName("Should Insert Element With No Collision")
    void shouldInsertElementWithNoCollision() {
        victim.put("a");
        victim.put("b");
        victim.put("c");

        assertNotNull(victim);
        assertFalse(victim.getPositionForValue("a").isEmpty());
        assertEquals(97, victim.getPositionForValue("a").get().getKey());
        assertEquals(0, victim.getPositionForValue("a").get().getValue().get());

        assertFalse(victim.getPositionForValue("b").isEmpty());
        assertEquals(98, victim.getPositionForValue("b").get().getKey());
        assertEquals(0, victim.getPositionForValue("b").get().getValue().get());

        assertFalse(victim.getPositionForValue("c").isEmpty());
        assertEquals(99, victim.getPositionForValue("c").get().getKey());
        assertEquals(0, victim.getPositionForValue("c").get().getValue().get());
        assertEquals(3, victim.getSize());
    }

    @Test
    @DisplayName("Should Insert Element With Collison")
    void shouldInsertElementWithCollision() {
        victim.put("Aa");
        victim.put("BB");

        assertNotNull(victim);

        assertEquals(2, victim.getSize());

        assertFalse(victim.getPositionForValue("Aa").isEmpty());
        assertEquals(64, victim.getPositionForValue("Aa").get().getKey());
        assertEquals(0, victim.getPositionForValue("Aa").get().getValue().get());

        assertFalse(victim.getPositionForValue("BB").isEmpty());
        assertEquals(64, victim.getPositionForValue("BB").get().getKey());
        assertEquals(1, victim.getPositionForValue("BB").get().getValue().get());
    }

    @Test
    @DisplayName("Should Get Token By Position")
    void shouldGetTokenByPosition() {
        victim.put("Aa");
        victim.put("BB");

        assertFalse(victim.getTokenForPosition(new MutablePair<>(64, new AtomicInteger(0))).isEmpty());
        assertEquals("Aa", victim.getTokenForPosition(new MutablePair<>(64, new AtomicInteger(0))).get());

        assertFalse(victim.getTokenForPosition(new MutablePair<>(64, new AtomicInteger(1))).isEmpty());
        assertEquals("BB", victim.getTokenForPosition(new MutablePair<>(64, new AtomicInteger(1))).get());

        assertTrue(victim.getTokenForPosition(new MutablePair<>(64, new AtomicInteger(2))).isEmpty());

    }

    @Test
    @DisplayName("Should Throw Exception")
    void shouldThrowException() {
        victim.put("x");

        assertEquals(1, victim.getSize());
        assertThrows(ExistentElementException.class, () -> victim.put("x"));
        assertEquals(1, victim.getSize());
    }
}