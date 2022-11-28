package grammar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Production {

    private List<String> productionNodes;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        productionNodes.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
