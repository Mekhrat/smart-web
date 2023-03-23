package kz.kaznu.smart.services;

import kz.kaznu.smart.models.dto.NewUserDto;
import kz.kaznu.smart.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> getUserByEmail(String email);
    Optional<User> getCurrentUser();
    User createNewUser(NewUserDto userDto);
    User save(User user);
}
