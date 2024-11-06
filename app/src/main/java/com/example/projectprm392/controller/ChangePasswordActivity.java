package com.example.projectprm392.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import com.example.projectprm392.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private String currentPassword;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnConfirmChangePassword;
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        currentPassword = intent.getStringExtra("password");
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        btnConfirmChangePassword = findViewById(R.id.btnConfirmChangePassword);
        btnConfirmChangePassword.setOnClickListener(view -> checkPasswords());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);

    }

    private void checkPasswords() {
        String enteredCurrentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (!enteredCurrentPassword.equals(currentPassword)) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
            return;
        }
        updatePassword(enteredCurrentPassword, newPassword, confirmPassword);
    }
    private void updatePassword(String currentPasswordInput, String newPasswordInput, String confirmPasswordInput) {
        String url = "http://10.0.2.2:7025/api/User/UpdatePassword/" + userID;
        JSONObject payload = new JSONObject();
        try {
            payload.put("currentPassword", currentPasswordInput);
            payload.put("newPassword", newPasswordInput);
            payload.put("confirmPassword", confirmPasswordInput);
            Log.d("Payload", payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Lỗi khi đổi mật khẩu. Vui lòng thử lại!";
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            errorMessage += " (Mã lỗi: " + statusCode + ")";
                            if (error.networkResponse.data != null) {
                                String responseBody = new String(error.networkResponse.data);
                                errorMessage += " Nội dung: " + responseBody; // In ra nội dung phản hồi từ server
                            }
                        }
                        Toast.makeText(ChangePasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
}