package uni.flcd.finalAutomata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transition {
    private State destState;
    private String value;

    @Override
    public String toString() {
        return " -- " + value + " --> " + destState.getLabel();
    }
}
