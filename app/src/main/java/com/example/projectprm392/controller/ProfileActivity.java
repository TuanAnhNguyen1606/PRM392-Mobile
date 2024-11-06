package com.example.projectprm392.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.R;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvUsername, tvEmail, tvRole;
    private Button btnChangePassword, btnLogout, btnMyOrder;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvRole = findViewById(R.id.tvRole);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyOrder = findViewById(R.id.btnMyOrder);
        loadUserInfo();
        btnChangePassword.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            intent.putExtra("password", password);
            startActivity(intent);
        });
        btnMyOrder.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyOrderActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            Logout();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }

    private void Logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userID", -1);

        if (userId == -1) {
            Log.e("ProfileActivity", "User ID not found in SharedPreferences");
            return;
        }
        String url = "http://10.0.2.2:7025/api/User/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String username = response.getString("username");
                            String email = response.getString("email");
                            String role = response.getString("role");
                            password = response.getString("password");
                            tvUsername.setText("Username: " + username);
                            tvEmail.setText("Email: " + email);
                            tvRole.setText("Role: " + role);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ProfileActivity", "Error fetching user info: " + error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }
}