package com.example.gateway.service;

import com.example.gateway.OrderDto;
import com.example.gateway.grpc.Order;
import com.example.gateway.grpc.OrderServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @GrpcClient("domainService")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceStub;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Cacheable(value = "orders", key = "#orderId")
    public OrderDto getOrder(String orderId) {
        Order.OrderRequest request = Order.OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        Order.OrderResponse orderResponse = orderServiceStub.getOrder(request);

        OrderDto orderDto = new OrderDto(orderResponse.getOrderId(),
                orderResponse.getCustomerName(),
                orderResponse.getAmount(),
                orderResponse.getDate());

        return orderDto;
    }

    @Cacheable(value = "orders", key = "'allOrders'")
    public List<OrderDto> getAllOrders() {
        Order.Empty emptyRequest = Order.Empty.newBuilder().build();

        Order.OrderList orderResponse = orderServiceStub.getAllOrders(emptyRequest);

        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order.OrderResponse o : orderResponse.getOrdersList()) {
            OrderDto orderDto = new OrderDto(o.getOrderId(),
                    o.getCustomerName(),
                    o.getAmount(),
                    o.getDate());
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    @CacheEvict(value = "orders", key = "#orderDto.orderId")
    public void createOrder(OrderDto orderDto) {
        rabbitTemplate.convertAndSend("orderExchange", "createOrderRoutingKey", orderDto);
    }
    @CacheEvict(value = "orders", key = "#orderDto.orderId")
    public void updateOrder(OrderDto orderDto) {
        rabbitTemplate.convertAndSend("orderExchange", "updateOrderRoutingKey", orderDto);
    }
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrder(String orderId) {
        rabbitTemplate.convertAndSend("orderExchange", "deleteOrderRoutingKey", orderId);
    }
}
