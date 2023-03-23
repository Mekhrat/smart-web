package kz.kaznu.smart.models.entities;

import lombok.*;

import javax.persistence.*;

@Table(name = "favorites")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Favorite extends BaseEntity{

    @ManyToOne
    private User user;

    @ManyToOne
    private Item item;
}
