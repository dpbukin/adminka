package com.example.domainservice.model;

import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "orders")
public class Order {
    private String orderId;
    private String customerName;
    private double amount;
    private String date;

    public Order() {
    }

    public Order(String orderId, String customerName, double amount, String date) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                '}';
    }
}