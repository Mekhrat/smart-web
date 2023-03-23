package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.dto.NewUserDto;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.services.MailSender;
import kz.kaznu.smart.services.UserService;
import kz.kaznu.smart.utils.ActivateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final MailSender mailSender;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }


    @GetMapping(value = "/register")
    public String auth(Model model) {
        model.addAttribute("newUser", new NewUserDto());
        return "register";
    }


    @PostMapping(value = "/register")
    public String register(@ModelAttribute("newUser") NewUserDto newUser) {
        if (newUser.getPassword().equals(newUser.getRePassword())) {
            Optional<User> user = userService.getUserByEmail(newUser.getEmail());

            User createdUser;
            if (user.isPresent() && !user.get().isActivated()) {
                createdUser = user.get();
            }
            else if (user.isEmpty() ) {
                createdUser = userService.createNewUser(newUser);
            }
            else {
                return "redirect:/register?error";
            }

            if (createdUser != null) {
                String code = UUID.randomUUID().toString();
                ActivateUtils.addActivateCodeForEmail(code, createdUser.getEmail());
                String text = ActivateUtils.activateMessageText(code);
                mailSender.send("Тіркелуді растау", text, createdUser.getEmail());
                return "redirect:/login";
            }
        }
        return "redirect:/register?error";

    }


    @GetMapping(value = "/activate/{code}")
    public String activate(@PathVariable(name = "code") String code) {
        String email = ActivateUtils.getEmailByActivateCode(code);
        if (email != null && !email.isEmpty()) {
             Optional<User> opUser = userService.getUserByEmail(email);
             if (opUser.isPresent()) {
                 User user = opUser.get();
                 user.setActivated(true);
                 userService.save(user);
                 return "redirect:/login?success";
             }
        }
        return "redirect:/login?error";
    }

}
