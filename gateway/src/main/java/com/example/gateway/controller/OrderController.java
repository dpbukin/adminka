package com.example.gateway.controller;

import com.example.gateway.OrderDto;
import com.example.gateway.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderId) {
        logger.info("Поиск заказа по ID: {}", orderId);
        OrderDto order = orderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        logger.info("Запрос на все заказы");
        List<OrderDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) {
        logger.info("Создание заказа: {}", orderDto);
        orderService.createOrder(orderDto);
        return new ResponseEntity<>("Запрос на создание заказа отправлен в очередь", HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable String orderId, @RequestBody OrderDto orderDto) {
        logger.info("Обновление заказа с ID: {} - {}", orderId, orderDto);
        orderService.updateOrder(orderDto);
        return new ResponseEntity<>("Запрос на обновление заказа отправлен в очередь", HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        logger.info("Удаление заказа с ID: {}", orderId);
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Запрос на удаление заказа отправлен в очередь", HttpStatus.OK);
    }
}
