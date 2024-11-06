package com.example.projectprm392.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.adapter.ProductAdapter;
import com.example.projectprm392.adapter.ProductHomeAdapter;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.example.projectprm392.entity.Brand;
import com.example.projectprm392.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.projectprm392.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView lvRecommendedLaptops;
    private ProductHomeAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private LinearLayout brandContainer;
    private List<Brand> brandList;
    private EditText searchProductTXT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lvRecommendedLaptops = findViewById(R.id.lv_recommended_laptops);
        brandContainer = findViewById(R.id.brandContainer);
        searchProductTXT = findViewById(R.id.et_search);
        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        brandList = new ArrayList<>();
        productAdapter = new ProductHomeAdapter(this, productList);
        lvRecommendedLaptops.setAdapter(productAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        
        fetchProducts();
        fetchBrands();

        searchProductTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchProducts(String productName) {
        filteredProductList.clear();
        if (productName.isEmpty()) {
            filteredProductList.addAll(productList);
            productAdapter.notifyDataSetChanged();
            return;
        }
        String url = "http://10.0.2.2:7025/api/Product/FindProductByName?productName=" + productName;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
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
                                filteredProductList.add(product);
                            }
                            // Update the adapter with the new filtered list
                            productAdapter.updateProductList(filteredProductList);
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Error fetching products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add request to RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    private void fetchProducts() {
        String url = "http://10.0.2.2:7025/api/Product";
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
                        filteredProductList.addAll(productList);
                        productAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Error fetching products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Thêm request vào RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void fetchBrands() {
        String url = "http://10.0.2.2:7025/api/Brand";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject brandObject = response.getJSONObject(i);
                                int brandID = brandObject.getInt("id");
                                String brandName = brandObject.getString("name");
                                Brand brand = new Brand(brandID, brandName);
                                brandList.add(brand);
                                Button brandButton = new Button(HomeActivity.this);
                                brandButton.setText(brandName);
                                brandButton.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                brandButton.setOnClickListener(v -> filterProductsByBrand(brandID));
                                brandContainer.addView(brandButton);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                    }
                });

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    private void filterProductsByBrand(int brandID) {
        String url = "http://10.0.2.2:7025/api/Product/GetProductByBrand/" + brandID; // Your API endpoint

        // Create a request to fetch products by brand ID
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        filteredProductList.clear(); // Clear the previous filtered list

                        // Parse the response and update the filteredProductList
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
                                filteredProductList.add(product); // Add the product to the filtered list
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Check if any products were found
                        if (filteredProductList.isEmpty()) {
                            Toast.makeText(HomeActivity.this, "No products found for the selected brand.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Filtered by brand ID: " + brandID, Toast.LENGTH_SHORT).show();
                        }

                        // Update the adapter with the new filtered list
                        productAdapter.updateProductList(filteredProductList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this, "Error fetching products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add request to RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


}
