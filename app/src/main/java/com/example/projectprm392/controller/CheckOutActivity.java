package com.example.projectprm392.controller;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.R;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.example.projectprm392.common.CartManager;
import com.example.projectprm392.entity.Address;
import com.example.projectprm392.entity.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class CheckOutActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etPhone, etProvince, etDistrict, etWard, etDetailAddress;
    private TextView tvTotalAmount;
    private Button btnSubmit;
    private List<CartItem> cartItems;
    private double totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etProvince = findViewById(R.id.etProvince);
        etDistrict = findViewById(R.id.etDistrict);
        etWard = findViewById(R.id.etWard);
        etDetailAddress = findViewById(R.id.etDetailAddress);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        double totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalAmount.setText("Total: " + numberFormat.format(totalPrice) + " VNĐ");
        cartItems = CartManager.getInstance().getCartItems();
        totalAmount = calculateTotalAmount();
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(CheckOutActivity.this, "Giỏ hàng của bạn đang trống, vui lòng thêm sản phẩm trước khi thanh toán.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!validateFields()) {
                return;
            }
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("userID", -1);
            Address address = new Address();
            address.setUserId(userId);
            address.setFirstName(etFirstName.getText().toString());
            address.setMiddleName(etMiddleName.getText().toString());
            address.setLastName(etLastName.getText().toString());
            address.setPhone(etPhone.getText().toString());
            address.setProvince(etProvince.getText().toString());
            address.setDistrict(etDistrict.getText().toString());
            address.setWard(etWard.getText().toString());
            address.setDetail(etDetailAddress.getText().toString());
            createAddress(address);
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
    }
    private boolean validateFields() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError("Please enter your first name");
            return false;
        }
        if (etMiddleName.getText().toString().trim().isEmpty()) {
            etMiddleName.setError("Please enter your middle name");
            return false;
        }
        if (etLastName.getText().toString().trim().isEmpty()) {
            etLastName.setError("Please enter your last name");
            return false;
        }
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Please enter your phone number");
            return false;
        }
        if (etProvince.getText().toString().trim().isEmpty()) {
            etProvince.setError("Please enter your province");
            return false;
        }
        if (etDistrict.getText().toString().trim().isEmpty()) {
            etDistrict.setError("Please enter your district");
            return false;
        }
        if (etWard.getText().toString().trim().isEmpty()) {
            etWard.setError("Please enter your ward");
            return false;
        }
        if (etDetailAddress.getText().toString().trim().isEmpty()) {
            etDetailAddress.setError("Please enter your detailed address");
            return false;
        }
        return true;
    }
    private double calculateTotalAmount() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getProduct().getProductPrice() * cartItem.getQuantity();
        }
        return total;
    }
    private void createAddress(Address address) {
        String url = "http://10.0.2.2:7025/api/Address";
        JSONObject addressJson = new JSONObject();
        try {
            addressJson.put("userId", address.getUserId());
            addressJson.put("firstName", address.getFirstName());
            addressJson.put("middleName", address.getMiddleName());
            addressJson.put("lastName", address.getLastName());
            addressJson.put("province", address.getProvince());
            addressJson.put("district", address.getDistrict());
            addressJson.put("ward", address.getWard());
            addressJson.put("detail", address.getDetail());
            addressJson.put("phone", address.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating address JSON", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, addressJson,
                response -> {
                    Toast.makeText(CheckOutActivity.this, "Address created successfully", Toast.LENGTH_SHORT).show();
                    try {
                        int shippingAddressId = response.getInt("id");
                        createOrder(address, shippingAddressId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    if (error.networkResponse != null) {
                        String errorMsg = new String(error.networkResponse.data);
                        Log.e("CheckoutError", "Error: " + errorMsg); // Log the error message
                        Toast.makeText(CheckOutActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("CheckoutError", "Error: " + error.getMessage()); // Log the error message
                        Toast.makeText(CheckOutActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
    @SuppressLint("NewApi")
    private void createOrder(Address address, int shippingAddressId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userID", -1);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject orderJson = new JSONObject();
        try {
            orderJson.put("userId", userId);
            orderJson.put("totalAmount", totalAmount);
            JSONArray orderDetailsArray = new JSONArray();
            for (CartItem cartItem : cartItems) {
                JSONObject orderDetail = new JSONObject();
                orderDetail.put("orderId", 0);
                orderDetail.put("productId", cartItem.getProduct().getProductID());
                orderDetail.put("quantity", cartItem.getQuantity());
                orderDetail.put("unitPrice", cartItem.getProduct().getProductPrice());
                orderDetailsArray.put(orderDetail);
            }
            orderJson.put("orderDetails", orderDetailsArray);
            orderJson.put("firstName", address.getFirstName());
            orderJson.put("middleName", address.getMiddleName());
            orderJson.put("lastName", address.getLastName());
            orderJson.put("province", address.getProvince());
            orderJson.put("district", address.getDistrict());
            orderJson.put("ward", address.getWard());
            orderJson.put("detail", address.getDetail());
            orderJson.put("phone", address.getPhone());
            orderJson.put("orderDate", LocalDate.now().toString());
            orderJson.put("shippingAddressId", shippingAddressId);
            orderJson.put("status", "Pending");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating order JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:7025/api/Order";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, orderJson,
                response -> {
                    Toast.makeText(CheckOutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    if (error.networkResponse != null) {
                        String errorMsg = new String(error.networkResponse.data);
                        Log.e("CheckoutError", "Error: " + errorMsg); // Log the error message
                        Toast.makeText(CheckOutActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("CheckoutError", "Error: " + error.getMessage()); // Log the error message
                        Toast.makeText(CheckOutActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

}