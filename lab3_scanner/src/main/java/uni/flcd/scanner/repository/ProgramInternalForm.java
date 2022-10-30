package uni.flcd.scanner.repository;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgramInternalForm {
    private static final String CONST = "const";
    private static final String ID = "id";
    @Getter
    private final List<Pair<String, Pair<Integer, AtomicInteger>>> programInternalForm;

    public ProgramInternalForm() {
        this.programInternalForm = new ArrayList<>();
    }

    public void addConstant(Pair<Integer, AtomicInteger> positionInST) {
        programInternalForm.add(Pair.of(CONST, positionInST));
    }

    public void addIdentifier(Pair<Integer, AtomicInteger> positionInST) {
        programInternalForm.add(Pair.of(ID, positionInST));
    }

    public void addReservedWord(String value) {
        programInternalForm.add(Pair.of(value, Pair.of(-1, new AtomicInteger(-1))));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ProgramInternalForm \n");
        programInternalForm
                .forEach(element -> {
                    stringBuilder.append(element.getLeft()).append(" ").append(element.getRight()).append("\n");
                });

        return stringBuilder.toString();
    }
}
