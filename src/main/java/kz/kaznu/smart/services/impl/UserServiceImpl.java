package kz.kaznu.smart.services.impl;

import kz.kaznu.smart.models.dto.EmployeeInfo;
import kz.kaznu.smart.models.dto.NewUserDto;
import kz.kaznu.smart.models.entities.Role;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.repositories.RoleRepository;
import kz.kaznu.smart.repositories.UserRepository;
import kz.kaznu.smart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opUser = userRepository.getUserByEmail(username);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (user.isActivated())
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
        }
        throw new UsernameNotFoundException("User not found");

    }

    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            org.springframework.security.core.userdetails.User secUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return getUserByEmail(secUser.getUsername());
        }
        return Optional.empty();
    }


    @Override
    public User createNewUser(NewUserDto userDto) {
        Role role = roleRepository.getRoleByRole("ROLE_USER");
        User user = User.builder()
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .isBlocked(false)
                .email(userDto.getEmail())
                .roles(Collections.singletonList(role))
                .activated(false)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateLastUser(User createdUser, NewUserDto newUser) {
        createdUser.setName(newUser.getName());
        createdUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(createdUser);
    }

    @Override
    public List<User> getAllCouriers() {
        Role role = roleRepository.getRoleByRole("ROLE_COURIER");
        return userRepository.findAllByRolesContaining(role);
    }

    @Override
    public List<User> getEmployees() {
        Role role = roleRepository.getRoleByRole("ROLE_USER");
        return userRepository.findAllByRolesNotContaining(role);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User createNewEmployee(EmployeeInfo employeeInfo) {
        Role role = roleRepository.getRoleByRole(employeeInfo.getRole());
        User user = User.builder()
                .name(employeeInfo.getName())
                .email(employeeInfo.getEmail())
                .activated(true)
                .address(employeeInfo.getAddress())
                .phone(employeeInfo.getPhone())
                .isBlocked(false)
                .password(passwordEncoder.encode(employeeInfo.getPassword()))
                .roles(Collections.singletonList(role))
                .build();

        return userRepository.save(user);
    }
}
