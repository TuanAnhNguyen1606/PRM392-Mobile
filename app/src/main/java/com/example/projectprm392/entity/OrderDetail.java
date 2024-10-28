package com.example.projectprm392.entity;

public class OrderDetail {
    private int OrderDetailID;
    private int OrderID;
    private int ProductID;
    private int Quantity;
    private double UnitPrice;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailID, int orderID, int productID, int quantity, double unitPrice) {
        OrderDetailID = orderDetailID;
        OrderID = orderID;
        ProductID = productID;
        Quantity = quantity;
        UnitPrice = unitPrice;
    }

    public int getOrderDetailID() {
        return OrderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        OrderDetailID = orderDetailID;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }
}
