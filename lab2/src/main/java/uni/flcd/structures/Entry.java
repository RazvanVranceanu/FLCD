package uni.flcd.structures;

import lombok.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class Entry {
    // left: position in array
    // right: position in linked list for collision
    @ToString.Include
    Pair<Integer, AtomicInteger> position;
    @ToString.Include
    private String token;
    private Entry next;
}
