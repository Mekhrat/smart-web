package kz.kaznu.smart.services;

import kz.kaznu.smart.models.dto.ConsumerInfo;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Order;
import kz.kaznu.smart.models.entities.User;

import java.util.List;

public interface OrderService {

    Order createNewOrder(User user, ConsumerInfo consumerInfo, List<Item> items);
    List<Order> getOrdersByUserEmail(String email);
    void cancelOrderById(Long orderId);
}
