package com.example.projectprm392.entity;

public class CartItem {
    private int cartItemID;
    private Product product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(int cartItemID, Product product, int quantity) {
        this.cartItemID = cartItemID;
        this.product = product;
        this.quantity = quantity;
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
