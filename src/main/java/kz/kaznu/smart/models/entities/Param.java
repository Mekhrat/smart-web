package kz.kaznu.smart.models.entities;

import kz.kaznu.smart.models.enums.ParamTypes;
import kz.kaznu.smart.models.enums.Params;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Table(name = "params")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Param extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private Params name;

    @Enumerated(EnumType.STRING)
    private ParamTypes type;
}
