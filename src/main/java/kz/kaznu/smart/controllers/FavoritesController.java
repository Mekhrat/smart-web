package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.entities.Favorite;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.services.FavoriteService;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class FavoritesController {
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final ItemService itemService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/favorites")
    public String getFavoriteItems(Model model) {
        User currentUser = userService.getCurrentUser().orElse(null);
        Optional<User> user = userService.getUserByEmail(currentUser.getEmail());
        model.addAttribute("currentUser", currentUser);
        if (user.isPresent()) {
            List<Item> favoriteItems = favoriteService.getFavoriteItemsByUser(user.get());
            model.addAttribute("items", favoriteItems);
            return "favorites";
        }
        return "redirect:/login";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/favorites")
    public String add(@RequestParam(name = "id", defaultValue = "0") Long id) {
        if (id == 0)
            return "redirect:/favorites";

        Optional<User> user = userService.getUserByEmail(userService.getCurrentUser().get().getEmail());
        Optional<Item> item = itemService.getItemById(id);
        if (user.isPresent() && item.isPresent()) {
            Optional<Favorite> favorite = favoriteService.getFavoriteItemsByUserAndItem(user.get(), item.get());
            if (favorite.isPresent()) {
                return "redirect:/favorites";
            }
            Favorite newFavoriteItem = Favorite.builder()
                    .item(item.get())
                    .user(user.get())
                    .build();
            favoriteService.save(newFavoriteItem);
            return "redirect:/favorites";
        }
        return "redirect:/favorites";
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/favorites")
    public String delete(@RequestParam(name = "id", defaultValue = "0") Long id) {
        if (id == 0)
            return "redirect:/favorites";

        Optional<User> user = userService.getUserByEmail("mekhrat_ashirbekov@mail.ru");
        Optional<Item> item = itemService.getItemById(id);
        if (user.isPresent() && item.isPresent()) {
            Optional<Favorite> favorite = favoriteService.getFavoriteItemsByUserAndItem(user.get(), item.get());
            favorite.ifPresent(favoriteService::delete);
        }
        return "redirect:/favorites";
    }
}
