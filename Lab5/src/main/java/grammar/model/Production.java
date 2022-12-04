package grammar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Production {

    @Builder.Default
    private List<String> productionNodes = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        productionNodes.forEach(s -> stringBuilder.append(s).append(" "));
        return stringBuilder.toString();
    }
}
