package kz.kaznu.smart.services.impl;

import kz.kaznu.smart.models.dto.ConsumerInfo;
import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Order;
import kz.kaznu.smart.models.entities.User;
import kz.kaznu.smart.models.enums.OrderStatus;
import kz.kaznu.smart.repositories.OrderRepository;
import kz.kaznu.smart.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order createNewOrder(User user, ConsumerInfo consumerInfo, List<Item> items) {
        Integer total = items.stream().map(Item::getNewPrice).reduce(Integer::sum).orElse(0);
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .consumerEmail(user.getEmail())
                .consumerIndex(consumerInfo.getIndex())
                .consumerName(consumerInfo.getName())
                .consumerPhone(consumerInfo.getPhone())
                .deliveryAddress(consumerInfo.getAddress())
                .status(OrderStatus.IN_PROCESSING)
                .items(items)
                .total(total)
                .build();
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.getAllOrdersByUserEmail(email);
    }

    @Override
    public void cancelOrderById(Long orderId) {
        Optional<Order> opOrder = orderRepository.findById(orderId);
        if (opOrder.isPresent()) {
            Order order = opOrder.get();
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.getAllByStatus(status);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }
}
