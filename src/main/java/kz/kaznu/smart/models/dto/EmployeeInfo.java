package kz.kaznu.smart.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfo {

    private String name;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String address;
}
