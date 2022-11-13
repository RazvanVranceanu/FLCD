package uni.flcd.finalAutomata.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class Transition {
    @EqualsAndHashCode.Exclude
    private State destState;
    private String value;
}
