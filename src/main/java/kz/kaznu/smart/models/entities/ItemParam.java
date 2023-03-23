package kz.kaznu.smart.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name = "item_params")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemParam extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    private Param param;

    private String value;

    private Integer orderValue;

}
