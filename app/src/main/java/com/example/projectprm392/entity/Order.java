package com.example.projectprm392.entity;

import java.time.LocalDate;

public class Order {
    private int OrderID;
    private LocalDate OrderDate;
    private double TotalAmount;
    private int UserID;

    public Order() {
    }

    public Order(int orderID, LocalDate orderDate, double totalAmount, int userID) {
        OrderID = orderID;
        OrderDate = orderDate;
        TotalAmount = totalAmount;
        UserID = userID;
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
}
