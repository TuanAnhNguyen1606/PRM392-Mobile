package com.example.projectprm392.controller;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.R;
import com.example.projectprm392.adapter.OrderAdapter;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.example.projectprm392.entity.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_order);

        RecyclerView orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        orderRecyclerView.setAdapter(orderAdapter);

        requestQueue = Volley.newRequestQueue(this);
        fetchOrders();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
    }

    private void fetchOrders() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", -1);

        if (userID != -1) {
            String url = "http://10.0.2.2:7025/api/User/Orders/" + userID;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    response -> {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject orderObject = response.getJSONObject(i);
                                Order order = new Order();
                                order.setOrderID(Integer.parseInt(orderObject.getString("id")));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    String orderDateString = orderObject.getString("orderDate");
                                    LocalDateTime orderDateTime = LocalDateTime.parse(orderDateString);
                                    LocalDate orderDate = orderDateTime.toLocalDate();
                                    order.setOrderDate(orderDate);
                                }
                                order.setTotalAmount(orderObject.getDouble("totalAmount"));
                                order.setStatus(orderObject.getString("status"));
                                orderList.add(order);
                                System.out.println("abcabcabc: " + order);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    },
                    error -> {
                        error.printStackTrace();
                    });
            requestQueue.add(jsonArrayRequest);
        } else {
            Log.e("MyOrderActivity", "UserID is null");
        }
    }

}