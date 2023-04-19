package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.dto.ConsumerInfo;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Order;
import kz.kaznu.smart.models.entities.Role;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.models.enums.OrderStatus;
import kz.kaznu.smart.services.ItemService;
import kz.kaznu.smart.services.OrderService;
import kz.kaznu.smart.services.UserService;
import kz.kaznu.smart.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final ItemService itemService;
    private final UserService userService;
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/orders")
    public String get(Model model) {
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent()) {
            List<Order> orders = orderService.getOrdersByUserEmail(currentUser.get().getEmail());
            model.addAttribute("orders", orders);
            model.addAttribute("currentUser", currentUser.get());
            return "orders";
        }
        return "redirect:/login";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/orders")
    public String newOrder(@ModelAttribute("consumerInfo") ConsumerInfo consumerInfo,
                           @CookieValue(value = "cart", defaultValue = "") String cart,
                           HttpServletResponse response,
                           Model model) {
        if (cart.equals("")) {
            model.addAttribute("items", new ArrayList<>());
            return "cart";
        }
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent()) {
            List<Long> ids = Utils.getIdsInString(cart);
            List<Item> cartItems = ids.stream().map(i -> itemService.getItemById(i).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            orderService.createNewOrder(currentUser.get(), consumerInfo, cartItems);
            response.addCookie(Utils.createNewCookie("cart", ""));
            return "redirect:/cart";
        }
        return "redirect:/login";
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/orders")
    public String delete(@RequestParam(name = "orderId") Long orderId,
                         Model model) {

        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent()) {
            orderService.cancelOrderById(orderId);
            return "redirect:/orders";
        }
        return "redirect:/login";
    }


    /**
     * ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders")
    public String adminOrders(Model model) {
        List<Order> newOrders = orderService.getOrdersByStatus(OrderStatus.IN_PROCESSING);
        List<Order> issuedToTheCourier = orderService.getOrdersByStatus(OrderStatus.ISSUED_TO_THE_COURIER);

        model.addAttribute("newOrders", newOrders);
        model.addAttribute("issuedToTheCourier", issuedToTheCourier);
        return "admin/orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders/{id}")
    public String adminOrder(@PathVariable(name = "id") Long id, Model model) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isPresent()) {
            List<User> couriers = userService.getAllCouriers();
            model.addAttribute("order", optionalOrder.get());
            model.addAttribute("couriers", couriers);
            return "admin/orderPage";
        }
        return "redirect:/admin/orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/orders")
    public String adminDelete(@RequestParam(name = "orderId") Long orderId, Model model) {
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent()) {
            orderService.cancelOrderById(orderId);
            return "redirect:/admin/orders";
        }
        return "redirect:/login";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/orders/toCourier")
    public String toCourier(@RequestParam(name = "orderId") Long id,
                            @RequestParam(name = "courier") String courier) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setCourierEmail(courier);
            order.setStatus(OrderStatus.ISSUED_TO_THE_COURIER);
            orderService.save(order);
        }
        return "redirect:/admin/orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/orders/toInProgress")
    public String toInProgress(@RequestParam(name = "orderId") Long id) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.IN_PROCESSING);
            orderService.save(order);
        }
        return "redirect:/admin/orders";
    }


    @GetMapping("/admin/report")
    public String report(Model model) {
        List<Order> orders = orderService.getOrdersByStatus(OrderStatus.DELIVERED);
        model.addAttribute("orders", orders);
        return "admin/report";
    }
}

