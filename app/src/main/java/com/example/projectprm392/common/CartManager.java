package com.example.projectprm392.common;

import com.example.projectprm392.entity.CartItem;
import com.example.projectprm392.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product, int quantity) {
        CartItem existingItem = null;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getProductName().equals(product.getProductName())) {
                existingItem = cartItem;
                break;
            }
        }
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cartItems.add(new CartItem(product.getProductID(), product, quantity));
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
