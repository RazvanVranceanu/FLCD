package uni.flcd.structures;

import lombok.Getter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import uni.flcd.exceptions.ExistentElementException;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SymbolTable {
    private static final int TABLE_CAPACITY = 256;
    private final Entry[] table;
    @Getter
    private Integer size = 0;

    public SymbolTable() {
        this.table = new Entry[TABLE_CAPACITY];
    }

    public void put(String value) {
        int index = getIndex(value);
        final AtomicInteger positionInLinkedList = new AtomicInteger(0);

        Entry newEntry = Entry.builder()
                .position(new MutablePair<>(index, positionInLinkedList))
                .token(value)
                .build();

        if (null == table[index]) {
            table[index] = newEntry;
            size++;
            return;
        }

        // it means that we have collision
        Entry previousNode = null;
        Entry currentNode = table[index];
        while (null != currentNode) {
            if (currentNode.getToken().equals(value)) {
                throw new ExistentElementException("Token with value " + value + " already exists in symbolTable");
            }
            previousNode = currentNode;
            currentNode = currentNode.getNext();
            positionInLinkedList.incrementAndGet();
        }

        if (null != previousNode) {
            previousNode.setNext(newEntry);
            size++;
        }
    }

    public Optional<Pair<Integer, AtomicInteger>> getPositionForValue(String value) {
        int index = getIndex(value);
        var currentNode = table[index];
        while (null != currentNode) {
            if (currentNode.getToken().equals(value)) {
                return Optional.ofNullable(currentNode.getPosition());
            }
            currentNode = currentNode.getNext();
        }
        return Optional.empty();
    }

    public Optional<String> getTokenForPosition(Pair<Integer, AtomicInteger> position) {

        var currentNode = table[position.getLeft()];
        var positionInList = position.getRight().get();
        while (null != currentNode) {
            if (currentNode.getPosition().getRight().get() == positionInList) {
                return Optional.ofNullable(currentNode.getToken());
            }
            currentNode = currentNode.getNext();
        }
        return Optional.empty();
    }

    private int getIndex(final String value) {
        return value.hashCode() % TABLE_CAPACITY;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SymbolTable \n");
        for (int i = 0; i < TABLE_CAPACITY; i ++) {
            var currentNode = table[i];
            while(null != currentNode) {
                stringBuilder.append(currentNode).append("\n");
                currentNode = currentNode.getNext();
            }
        }
        return stringBuilder.toString();
    }
}
