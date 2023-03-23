package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.dto.ConsumerInfo;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.UserService;
import kz.kaznu.smart.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping(value = "/cart")
    public String getCart(@CookieValue(value = "cart", defaultValue = "") String cart, Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser().orElse(null));
        if (cart.equals("")){
            model.addAttribute("items", new ArrayList<>());
            return "cart";
        }
        List<Long> ids = Utils.getIdsInString(cart);
        List<Item> cartItems = ids.stream().map(i -> itemService.getItemById(i).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        model.addAttribute("items", cartItems);

        Integer sum = cartItems.stream().map(Item::getNewPrice).reduce(Integer::sum).orElse(0);
        model.addAttribute("sum", sum);
        model.addAttribute("consumerInfo", new ConsumerInfo());

        return "cart";
    }


    @PostMapping("/cart")
    public String addItemToCart(@RequestParam(name = "id") Long id,
                                @CookieValue(value = "cart", defaultValue = "") String cart,
                                HttpServletResponse response) {
        List<Long> ids = Utils.getIdsInString(cart);
        if (ids.contains(id)) {
            return "redirect:/cart";
        }
        response.addCookie(Utils.createNewCookie("cart", cart + "s" + id.toString()));
        return "redirect:/cart";
    }


    @DeleteMapping("/cart")
    public String delete(@RequestParam(name = "id") Long id,
                         @CookieValue(value = "cart", defaultValue = "") String cart,
                         HttpServletResponse response) {
        List<Long> ids = Utils.getIdsInString(cart);
        if (!ids.contains(id)) {
            return "redirect:/cart";
        }
        String newCookieValue = Utils.deleteIdInArray(ids, id);
        response.addCookie(Utils.createNewCookie("cart", newCookieValue));
        return "redirect:/cart";
    }
}
