package kz.kaznu.smart.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerInfo {
    private String name;
    private String phone;
    private String address;
    private String index;
}
