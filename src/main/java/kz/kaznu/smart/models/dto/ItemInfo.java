package kz.kaznu.smart.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfo {

    private Long id;
    private String name;
    private String fullName;
    private Integer price;
    private Integer lastPrice;
    private Integer quantity;
    private String brand;
    private String type;
}
