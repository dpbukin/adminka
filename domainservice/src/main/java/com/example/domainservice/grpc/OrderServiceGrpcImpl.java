package com.example.domainservice.grpc;

import com.example.domainservice.repository.OrderRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class OrderServiceGrpcImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceGrpcImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void getOrder(Order.OrderRequest request, StreamObserver<Order.OrderResponse> responseObserver) {
        try {
            com.example.domainservice.model.Order order = orderRepository.findByOrderId(request.getOrderId()).orElse(null);
            System.out.println(order);

            if (order != null) {
                Order.OrderResponse response = Order.OrderResponse.newBuilder()
                        .setOrderId(order.getOrderId())
                        .setCustomerName(order.getCustomerName())
                        .setAmount(order.getAmount())
                        .setDate(order.getDate() != null ? order.getDate() : "")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                System.out.println("gRPC запрос getOrder обработан успешно");
            } else {
                responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Order not found")));
            }
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription("Internal error")));
        }
    }

    @Override
    public void getAllOrders(Order.Empty request, StreamObserver<Order.OrderList> responseObserver) {
        try {
            List<com.example.domainservice.model.Order> orders = orderRepository.findAll();
            System.out.println(orders);


            Order.OrderList.Builder orderListBuilder = Order.OrderList.newBuilder();

            for (com.example.domainservice.model.Order order : orders) {

                Order.OrderResponse orderResponse = Order.OrderResponse.newBuilder()
                        .setOrderId(order.getOrderId())
                        .setCustomerName(order.getCustomerName())
                        .setAmount(order.getAmount())
                        .setDate(order.getDate() != null ? order.getDate() : "")
                        .build();

                orderListBuilder.addOrders(orderResponse);
            }

            responseObserver.onNext(orderListBuilder.build());
            responseObserver.onCompleted();
            System.out.println("gRPC запрос getAllOrders обработан успешно");

        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription("Internal error")));
        }
    }
}
