package structures;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SymbolTableTest {
	private SymbolTable<Integer, String> victim;

	@BeforeEach
	void setup() {

		victim = new SymbolTable<>();
	}

	@Test
	@DisplayName("Should Add Four New Elements")
	void shouldAddFourNewElements() {
		victim.put(1, "a");
		victim.put(2, "b");
		victim.put(3, "c");
		victim.put(256, "d");

		assertNotNull(victim);
		assertEquals(4, victim.getSize());
		assertEquals(Optional.of("a"), victim.getValue(1));
		assertEquals(Optional.of("b"), victim.getValue(2));
		assertEquals(Optional.of("c"), victim.getValue(3));
		assertEquals(Optional.of("d"), victim.getValue(256));
	}

	@Test
	@DisplayName("Should Update Value For Key")
	void shouldUpdateValueForKey() {
		victim.put(1, "a");
		victim.put(1, "b");

		assertNotNull(victim);
		assertEquals(1, victim.getSize());
		assertEquals(Optional.of("b"), victim.getValue(1));
	}
}