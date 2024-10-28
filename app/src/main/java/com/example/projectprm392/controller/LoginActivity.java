    package com.example.projectprm392.controller;

    import static com.example.projectprm392.common.APIUrl.userAPIURL;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;


    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonObjectRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.projectprm392.MainActivity;
    import com.example.projectprm392.R;

    import org.json.JSONException;
    import org.json.JSONObject;

    public class LoginActivity extends AppCompatActivity {
        EditText LoginEmail, LoginPassword;
        Button btnLogin;
        CheckBox checkBoxRememberMe;
        TextView register;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);
            LoginEmail = findViewById(R.id.LoginEmail);
            LoginPassword = findViewById(R.id.LoginPassword);
            checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
            register = findViewById(R.id.textViewRegister);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            btnLogin = findViewById(R.id.buttonLogin);
            btnLogin.setOnClickListener(v -> {
                loginUser();
            });
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            if (sharedPreferences.getBoolean("rememberMe", false)) {
                String savedEmail = sharedPreferences.getString("email", "");
                LoginEmail.setText(savedEmail);
                checkBoxRememberMe.setChecked(true);
            }
        }
        private void loginUser() {
            String email = LoginEmail.getText().toString().trim();
            String password = LoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = "http://10.0.2.2:7025/api/User/login";
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("email", email);
                jsonBody.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    response -> {
                        try {
                            String userEmail = response.getString("email");
                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", userEmail);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        if (error.networkResponse != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Toast.makeText(LoginActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }
    }