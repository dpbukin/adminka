package com.example.domainservice.repository;


import com.example.domainservice.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findAll();
    @Query("{ 'orderId' : ?0 }")
    Optional<Order> findByOrderId(String orderId);
}
