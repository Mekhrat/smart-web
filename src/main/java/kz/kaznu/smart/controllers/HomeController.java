package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.enums.ItemType;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping(value = "/home")
    public String home(Model model) {
        Page<Item> items = itemService.getTopItemsByType(ItemType.SMARTPHONE,0, 6);
        model.addAttribute("currentUser", userService.getCurrentUser().orElse(null));
        model.addAttribute("items", items);
        model.addAttribute("smartwhaches", itemService.getTopItemsByType(ItemType.SMARTWATCH,0, 6));
        model.addAttribute("headphones", itemService.getTopItemsByType(ItemType.HEADPHONE,0, 6));

        return "index";
    }
}
