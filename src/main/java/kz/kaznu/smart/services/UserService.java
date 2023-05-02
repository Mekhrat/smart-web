package kz.kaznu.smart.services;

import kz.kaznu.smart.models.dto.EmployeeInfo;
import kz.kaznu.smart.models.dto.NewUserDto;
import kz.kaznu.smart.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> getUserByEmail(String email);
    Optional<User> getCurrentUser();
    User createNewUser(NewUserDto userDto);
    User save(User user);

    void updateLastUser(User createdUser, NewUserDto newUser);

    List<User> getAllCouriers();

    List<User> getEmployees();

    Optional<User> getById(Long id);

    void delete(User user);

    User createNewEmployee(EmployeeInfo employeeInfo);
}
