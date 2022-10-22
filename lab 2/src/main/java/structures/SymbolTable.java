package structures;

import java.util.Optional;

public class SymbolTable<K, V> {
	private final Entry<K, V>[] table;
	private static final int INITIAL_TABLE_CAPACITY = 128;
	private int size = 0;

	public SymbolTable() {
		this.table = new Entry[INITIAL_TABLE_CAPACITY];
	}

	public void put(K key, V value) {
		final Entry<K, V> entry = Entry.<K, V>builder()
				.key(key)
				.value(value)
				.next(null)
				.build();

		int index = getIndex(key);

		if (null == table[index]) {
			table[index] = entry;
			size++;
			return;
		}

		Entry<K, V> currentNode = table[index];
		while (null != currentNode) {
			if (currentNode.getKey().equals(key)) {
				table[index].setValue(value);
				return;
			}
			currentNode = currentNode.getNext();
		}

		//TODO: vezi cand ai coliziune ce faci (de aici in jos)
		if (null != currentNode.getNext()) {
			currentNode.setNext(entry);
			size++;
		}

	}

	public Optional<V> getValue(K key) {
		Entry<K, V> currentNode = table[getIndex(key)];
		while(null != currentNode) {
			if(currentNode.getKey().equals(key)) {
				return Optional.ofNullable(currentNode.getValue());
			}
			currentNode = currentNode.getNext();
		}
		return Optional.empty();
	}

	public int getSize() {
		return this.size;
	}

	public boolean containsKey(K key) {
		Entry<K, V> currentNode = table[getIndex(key)];
		while(null != currentNode) {
			if(currentNode.getKey().equals(key)) {
				return true;
			}
			currentNode = currentNode.getNext();
		}
		return false;
	}

	private int getIndex(final K key) {
		return key.hashCode() % getTableCapacity();
	}

	private int getTableCapacity() {
		return table.length;
	}
}
