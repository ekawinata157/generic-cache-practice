package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcreteModel {
    private long id;
    private String name;

    public static ConcreteModel getConcreteModel(long id, String name) {
        return ConcreteModel.builder()
                .name(name)
                .id(id)
                .build();
    }
}
