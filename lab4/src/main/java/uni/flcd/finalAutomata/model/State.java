package uni.flcd.finalAutomata.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class State {
    private String label;
    private StateType stateType;
}
