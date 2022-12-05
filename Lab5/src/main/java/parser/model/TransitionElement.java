package parser.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import parser.model.enums.Type;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransitionElement {

    private String value;
    private int index;
    private Type type;
}
