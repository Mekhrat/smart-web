package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.dto.EmployeeInfo;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/employees")
    public String getEmployee(Model model) {
        List<User> employees = userService.getEmployees();
        model.addAttribute("employees", employees);
        return "admin/employees";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/employee")
    public String delete(@RequestParam(name = "id") Long id, Model model) {
        Optional<User> opEmployee = userService.getById(id);
        opEmployee.ifPresent(userService :: delete);
        return "redirect:/admin/employees";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/employee")
    public String createEmployee(Model model) {
        model.addAttribute("employeeInfo", new EmployeeInfo());
        return "admin/employee";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/employee")
    public String create(@ModelAttribute("employeeInfo") EmployeeInfo employeeInfo) {
        Optional<User> opUser = userService.getUserByEmail(employeeInfo.getEmail());
        if (opUser.isEmpty()) {
            userService.createNewEmployee(employeeInfo);
            return "redirect:/admin/employee?success";
        }
        return "redirect:/admin/employee?error";
    }
}
