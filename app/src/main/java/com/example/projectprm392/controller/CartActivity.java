package com.example.projectprm392.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.adapter.CartAdapter;
import com.example.projectprm392.adapter.OnCartUpdatedListener;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.example.projectprm392.common.CartManager;
import com.example.projectprm392.entity.CartItem;
import com.example.projectprm392.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements OnCartUpdatedListener {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView totalCartPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        cartItemList = CartManager.getInstance().getCartItems();
        totalCartPrice = findViewById(R.id.text_total);
        cartAdapter = new CartAdapter(this, cartItemList, this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        updateTotalCartPrice();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cart);
    }
    @Override
    public void onCartUpdated() {
        updateTotalCartPrice();
    }
    private void updateTotalCartPrice() {
        double total = cartAdapter.getTotalCartPrice();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        totalCartPrice.setText("Total: " + numberFormat.format(total) + " VNĐ");
    }


}