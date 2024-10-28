package com.example.projectprm392.controller;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.R;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.example.projectprm392.adapter.ProductAdapter;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.example.projectprm392.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShopActivity extends AppCompatActivity {
    private ListView listView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        listView = findViewById(R.id.listViewProducts);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        listView.setAdapter(productAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_shop);
        fetchProducts();
    }

    private void fetchProducts() {
        String url = "http://10.0.2.2:7025/api/Product"; // Địa chỉ API của bạn
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject productObject = response.getJSONObject(i);
                                int productID = productObject.getInt("productID");
                                String productName = productObject.getString("productName");
                                double productPrice = productObject.getDouble("price");
                                String productImage = productObject.getString("imageUrl");
                                String productDescription = productObject.getString("description");
                                int stock = productObject.getInt("stock");
                                int brandID = productObject.getInt("brandID");
                                String releaseDate = productObject.getString("releaseDate");
                                Product product = new Product(productID, productName, productDescription, productPrice, stock, productImage, releaseDate, brandID);
                                productList.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShopActivity.this, "Error fetching products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Thêm request vào RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}