package com.example.projectprm392.entity;

import java.time.LocalDate;

public class Order {
    private int OrderID;
    private LocalDate OrderDate;
    private double TotalAmount;
    private int UserID;
    private int ShippingAddressID;
    private String Status;

    public Order() {
    }

    public Order(int orderID, LocalDate orderDate, double totalAmount, int userID, int shippingAddressID, String status) {
        OrderID = orderID;
        OrderDate = orderDate;
        TotalAmount = totalAmount;
        UserID = userID;
        ShippingAddressID = shippingAddressID;
        Status = status;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        OrderDate = orderDate;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getShippingAddressID() {
        return ShippingAddressID;
    }

    public void setShippingAddressID(int shippingAddressID) {
        ShippingAddressID = shippingAddressID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
