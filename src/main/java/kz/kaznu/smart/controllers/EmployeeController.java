package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final UserService userService;

//    @GetMapping("/admin/employee")
//    public String getEmployee(Model model) {
//        List<User> employees = userService.getEmployees();
//        model.addAttribute("employees", employees);
//
//    }
}
