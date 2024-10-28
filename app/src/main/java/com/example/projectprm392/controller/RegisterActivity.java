package com.example.projectprm392.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText email, username, password, repassword;
    Button buttonRegister;
    TextView txtLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.txtInputEmail);
        username = findViewById(R.id.txtInputUsername);
        password = findViewById(R.id.txtInputPassword);
        repassword = findViewById(R.id.txtInputRepassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        buttonRegister.setOnClickListener(v -> {
            registerUser();
        });

    }

    private void registerUser() {
        String emailText = email.getText().toString().trim();
        String usernameText = username.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String rePasswordText = repassword.getText().toString().trim();
        if (emailText.isEmpty() || usernameText.isEmpty() || passwordText.isEmpty() || rePasswordText.isEmpty()) {
            Toast.makeText(this, "Hãy điền đầy đủ tất cả các ô", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!passwordText.equals(rePasswordText)) {
            Toast.makeText(this, "Passwords không khớp nhau", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://10.0.2.2:7025/api/User/register";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Email", emailText);
            jsonBody.put("UserName", usernameText);
            jsonBody.put("Password", passwordText);
            jsonBody.put("RePassword", rePasswordText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    // Xử lý khi đăng ký thành công
                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    String errorMsg;
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data);
                            JSONObject errorJson = new JSONObject(errorResponse);
                            errorMsg = errorJson.getString("message");
                        } catch (JSONException e) {
                            errorMsg = "An error occurred.";
                        }
                    } else {
                        errorMsg = "Cannot connect to server";
                    }
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                }
        );

        // Thêm request vào hàng đợi của Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}