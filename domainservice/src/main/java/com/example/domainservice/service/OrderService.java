package com.example.domainservice.service;

import com.example.domainservice.model.Order;
import com.example.domainservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @RabbitListener(queues = "createOrderQueue")
    public void handleCreateOrder(Order orderDto) {
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setCustomerName(orderDto.getCustomerName());
        order.setAmount(orderDto.getAmount());
        order.setDate(orderDto.getDate());

        orderRepository.save(order);
        System.out.println("Заказ создан: " + orderDto.getOrderId());
    }

    @RabbitListener(queues = "updateOrderQueue")
    public void handleUpdateOrder(Order orderDto) {
        Order order = orderRepository.findById(orderDto.getOrderId()).orElse(null);
        if (order != null) {
            order.setCustomerName(orderDto.getCustomerName());
            order.setAmount(orderDto.getAmount());

            orderRepository.save(order);
            System.out.println("Заказ обновлен: " + orderDto.getOrderId());
        }
    }

    @RabbitListener(queues = "deleteOrderQueue")
    public void handleDeleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
            System.out.println("Заказ удален: " + order.getOrderId());
        }
    }
}
