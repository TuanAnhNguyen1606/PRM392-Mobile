package com.example.projectprm392.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.R;
import com.example.projectprm392.common.BottomNavigationHandler;
import com.example.projectprm392.common.CartManager;
import com.example.projectprm392.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    Button btnAddToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        btnAddToCart = findViewById(R.id.button_add_to_cart);
        // Nhận Intent
        int productID = getIntent().getIntExtra("PRODUCT_ID", 0);
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        double productPrice = getIntent().getDoubleExtra("PRODUCT_PRICE", 0);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(productPrice) + " VNĐ";
        String productDescription = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        String productImage = getIntent().getStringExtra("PRODUCT_IMAGE");

        // Thiết lập các view
        TextView tvProductName = findViewById(R.id.text_product_name);
        TextView tvProductPrice = findViewById(R.id.text_product_price);
        TextView tvProductDescription = findViewById(R.id.text_product_description);
        ImageView ivProductImage = findViewById(R.id.image_product);

        // Hiển thị thông tin sản phẩm
        tvProductName.setText(productName);
        tvProductPrice.setText(formattedPrice);
        tvProductDescription.setText(productDescription);

        // Sử dụng Picasso để tải hình ảnh
        Picasso.get().load(productImage).into(ivProductImage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        BottomNavigationHandler.setupBottomNavigation(bottomNavigationView, this);
        
        btnAddToCart.setOnClickListener(v -> {
            AddToCart();
        });
    }
    private void AddToCart() {
        // Nhận thông tin sản phẩm từ Intent
        int productID = getIntent().getIntExtra("PRODUCT_ID", 0);
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        double productPrice = getIntent().getDoubleExtra("PRODUCT_PRICE", 0);
        String productDescription = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        String productImage = getIntent().getStringExtra("PRODUCT_IMAGE");
        int stock = getIntent().getIntExtra("PRODUCT_STOCK", 0); // Thêm thông tin về stock
        if (stock <= 0) {
            Toast.makeText(this, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo sản phẩm mới và thêm vào giỏ hàng
        Product product = new Product(productID, productName, productDescription, productPrice, stock, productImage, null, 1);
        CartManager.getInstance().addToCart(product, 1);
        Toast.makeText(this, "Sản phẩm đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
    }

}