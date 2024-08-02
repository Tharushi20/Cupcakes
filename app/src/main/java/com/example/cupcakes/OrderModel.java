package com.example.cupcakes;

import java.util.Map;

public class OrderModel {
    private String orderId;
    private String customerName;
    private String address;
    private String phone;
    private String paymentMethod;
    private double totalAmount;
    private String userId;



    private Map<String, Map<String, Object>> items;


    public OrderModel() {

    }

    public OrderModel(String orderId, String customerName, String address, String phone, String paymentMethod, double totalAmount, String customerId) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.userId = customerId;
    }

    public OrderModel(String orderId, String customerName, String address, String phone, String paymentMethod, double totalAmount, String customerId, Map<String, Map<String, Object>> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.userId = customerId;
        this.items = items;
    }

    public OrderModel(String customerName, String address, String phone, String paymentMethod, double totalAmount) {
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }

    public OrderModel(String orderId, String customerName, String address, String phone, String paymentMethod, double totalAmount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }

    public OrderModel(String orderId, String customerName, String address, String phone, String paymentMethod, double totalAmount, Map<String, Map<String, Object>> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.items = items;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String customerId) {
        this.userId = customerId;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Map<String, Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(Map<String, Map<String, Object>> items) {
        this.items = items;
    }
}
